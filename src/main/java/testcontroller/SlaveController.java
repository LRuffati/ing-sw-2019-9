package testcontroller;

import actions.ActionBundle;
import actions.ActionTemplate;
import actions.effects.Effect;
import actions.effects.ReloadTemplate;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmountUncapped;
import actions.utils.ChoiceMaker;
import actions.utils.PowerUpType;
import board.Sandbox;
import genericitems.Tuple;
import grabbables.PowerUp;
import network.Database;
import grabbables.Weapon;
import network.Player;
import network.ServerInterface;
import player.Actor;
import testcontroller.controllermessage.*;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is still going to be part of the server but it represents a single player
 * Some of its methods will be exposed over the network to communicate with the client
 *
 * However we can assume that the network layer
 *
 * Not all interactions between player and game need to go through this class, however it acts as
 * a gateway, for instance when it's time to choose an action for the round the client will ask
 * for the next step, this class will:
 * + retrieve the correct ActionBundle
 * + return the «pickaction, actionbundle»
 * Then the interaction will only be with the action bundle till the end of the building phase,
 * at which point the client receives a TERMINATED signal and this class receives the call to
 * initiate merging of the effects.
 *
 * The client will receive a TERMINATED signal, but the SlaveController will already know that,
 * for instance it needs to resolve a GRABEFFECT, so:
 * 1. Client sees terminated
 * 2. Client asks "what next?"
 * 3. Request is forwarded to SlaveController
 * 4. SlaveController returns «PICKWEAPON, ...»
 * 5. The network layer forwards this to the client
 *
 * From the point of view of the Main controller the SlaveController has different functions for
 * each possible interaction line
 */
public class SlaveController {
    private Player player;
    private ServerInterface network;
    private ControllerMessage currentMessage;
    public final MainController main;
    private List<String> notificationList;
    private Runnable onTimeout;
    private int timeoutWindow;

    public SlaveController(MainController main, Player player, ServerInterface network) {
        this.player = player;
        this.network = network;
        this.setCurrentMessage(new WaitMessage(List.of()));
        this.main = main;
        this.timeoutWindow = main.timeoutTime;
    }

    /**
     * This function sets in motion the main turn line
     */
    public void startMainAction(){
        this.onTimeout = () -> {
            main.endTurn(getSelf());
        };
        this.setCurrentMessage(setPowUps(new ArrayList<>(), getSelf().getActions()));
    }

    /**
     * The finalizer of an ActionBundle or start main action calls this function and sets the
     * slave controller message to the result.
     * The result makes you pick powerups, then resolves them and the finalizer returns a
     * PickActionMessage(nextActs.get(0)), if nextActs is empty then it starts the reload instead
     *
     * @param lastEffects The effects I need to filter the powerupList by
     * @param nextActs Each list will create an actionTemplate
     * @return the action that should be executed next
     */
    private ControllerMessage setPowUps(List<Effect> lastEffects,
                                      List<List<ActionTemplate>> nextActs){

        // Which powerups can I use
        List<PowerUp> pows = getSelf().getPowerUp().stream()
                                    .filter(i->i.canUse(lastEffects))
                                    .collect(Collectors.toList());

        Sandbox sandbox = getSelf().getGm().createSandbox(getSelf().pawnID());

        Function<Sandbox, ControllerMessage> reloadMerger = // Will take the effects in
                // sandbox and
                sandbox1 -> { // merge them into MainController
                    this.setCurrentMessage(new WaitMessage(List.of()));
                    List<Effect> effects = sandbox1.getEffectsHistory();
                    Runnable onResolved = () -> this.main.endTurn(this.getSelf());
                    new Thread(()-> this.main.resolveEffect(this, effects, onResolved)).start();
                    return new WaitMessage(List.of());
        };

        // Used if I'll have to reload next
        ControllerMessage reloadMessage = new ReloadTemplate().spawn(Map.of(), sandbox, reloadMerger);

        // Used if I might run an actionbundle after
        Function<List<List<ActionTemplate>>, Function<List<Effect>, ControllerMessage>> bundleFinalizer =
                bundlesTail -> effectList -> {
                    this.setCurrentMessage(new WaitMessage(List.of()));

                    Runnable onResolved = () -> this.setCurrentMessage(this.setPowUps(effectList, bundlesTail));

                    new Thread(() -> main.resolveEffect(this, effectList, onResolved)).start();

                    return new WaitMessage(List.of());
                };

        List<List<ActionTemplate>> tail = nextActs.subList(1, nextActs.size());
        ActionBundle action = new ActionBundle(sandbox, nextActs.get(0), bundleFinalizer.apply(tail));
        ControllerMessage actionMessage = new PickActionMessage(action, "Scegli un'azione",
                sandbox, getNotifications());

        if ((pows.isEmpty()) & (nextActs.isEmpty())){ // No powUp available, only reload left
            return reloadMessage;

        } else if ((pows.isEmpty()) & !nextActs.isEmpty()){ // No powerups but ActionBundle
            // available
            return actionMessage;

        } else { // Powerups and then reload
            Function<List<PowerUp>, ControllerMessage> onPowupPick =
                    list -> {
                        if (list.isEmpty()) {
                            if (nextActs.isEmpty()) {
                                return reloadMessage;
                            } else {
                                return actionMessage;
                            }
                        } else {
                            //3. Call this function with same params
                            Runnable onApplied = () -> this.setCurrentMessage(this.setPowUps(lastEffects, nextActs));
                            //2. Apply powerup
                            return list.get(0).usePowup(this, lastEffects, onApplied);
                        }
                    };
            return new PickPowerupMessage(SlaveControllerState.MAIN, pows, onPowupPick,
                    "Scegli un powerup da usare", true, getNotifications());
        }
    }

    private List<String> getNotifications() {
        List<String> notifs = List.copyOf(notificationList);
        notificationList.clear();
        return notifs;

    }

    /**
     * This function is invoked when the player needs to respawn
     *
     * It is called by the Main controller after it already picked the two cards from the deck
     */
    public void startRespawn(Consumer<PowerUp> onRespawned){
        List<PowerUp> powups = new ArrayList<>(getSelf().getPowerUp());

        Function<List<PowerUp>, ControllerMessage> onPick =
                list -> {
                    this.setCurrentMessage(new WaitMessage(List.of()));
                    new Thread(()-> {
                        onRespawned.accept(list.get(0));
                    }).start();
                    return new WaitMessage(List.of());
                };

        // If timeout is triggered I should spawn in a random location
        // onRespawned is blocking so I should start it in a thread
        this.onTimeout = () -> new Thread(() -> onRespawned.accept(powups.get(0))).start();

        this.setCurrentMessage(new PickPowerupMessage(
                SlaveControllerState.RESPAWN,
                powups,
                onPick,
                "Scegli lo spawn point",
                true,
                List.of()
                ));
    }

    /**
     * This method is called when a player is damaged by another player and has one or more
     * takeback grenades in his stack
     *
     * @param offender The player which caused the damage
     * @param onFinished Tells the Main whether the player will use the tagback grenade and which
     *                  one. The provider should make sure it is only used once. Blocking
     */
    public void startTagback(Actor offender, Consumer<Optional<PowerUp>> onFinished){
        Sandbox sandbox = getSelf().getGm().createSandbox(getSelf().pawnID());
        BasicTarget other = sandbox.getBasic(offender.pawnID());

        if (other.seen(sandbox, sandbox.getBasic(getSelf().pawnID()), false)){
            List<PowerUp> tagbacks =
                    getSelf().getPowerUp().stream().filter(powerUp -> powerUp.getType().equals(PowerUpType.TAGBACKGRANADE)).collect(Collectors.toList());

            if (tagbacks.isEmpty()) {
                onFinished.accept(Optional.empty());
                return;
            }

            ControllerMessage powerupPicker =
                    new PickPowerupMessage(SlaveControllerState.USETAGBACK,
                            tagbacks, list -> {
                        if (list.isEmpty()) {
                            onFinished.accept(Optional.empty());
                        } else {
                            onFinished.accept(Optional.of(list.get(0)));
                        }
                        return new WaitMessage(List.of());
                    }, "Scegli quale tagback usare", true, getNotifications());

            ChoiceMaker choiceMaker = new ChoiceMaker() {
                @Override
                public void giveTargets(String targetId, List<TargetView> possibilities,
                                        Function<Integer, Targetable> action){
                    // Since targets aren't added by a targeter but provided at creation this
                    // method is unnecessary
                }

                @Override
                public Tuple<Boolean, List<TargetView>> showOptions() {
                    return new Tuple<>(true,
                            List.of(other.generateView(sandbox)));
                }

                @Override
                public ControllerMessage pick(int choice) {
                    if (choice<0){
                        onFinished.accept(Optional.empty());
                        return new WaitMessage(List.of());
                    } else {
                        return powerupPicker;
                    }
                }
            };

            this.onTimeout = () -> new Thread( () -> onFinished.accept(Optional.empty())).start();
            this.setCurrentMessage(new PickTargetMessage(choiceMaker,
                    "Vuoi dare un marchio a questo giocatore?", sandbox));

        } else {
            onFinished.accept(Optional.empty());
        }

    }

    /**
     * This method is called by the client (through the network) when the client doesn't have any
     * instruction left to complete.
     * This can happen for instance after the client, having completed the choice selection
     * started by an action bundle, receives the TERMINATED/WAIT signal
     *
     * At that point the client doesn't have any information on what should happen next and calls
     * the getInstruction method on its SlaveController
     *
     * Another use case is in the case of a catastrophic failure of the client, ignoring time
     * limitations which make this exemplificatory scenario unlikely, a client might lose all
     * recorded information about an action, upon rejoining and being bound to the same
     * SlaveController the SlaveController will still return the last action the user had to
     * execute prior to the failure
     *
     * @return the last valid ControllerMessage generated by the Controller for the Client, the
     * value doesn't change until a new command is available (meaning the
     */
    public ControllerMessage getInstruction(){
        ControllerMessage mess = getCurrentMessage();
        if (mess.type().equals(SlaveControllerState.WAIT)) {
            List<String> old = new ArrayList<>(mess.getMessage().getChanges());
            old.addAll(getNotifications());
            setCurrentMessage(new WaitMessage(List.of()));
            return new WaitMessage(old);
        }

        else return mess;
    }

    void onConnection(Player player, int numOfPlayer) {
        try {
            network.onConnection(player, numOfPlayer);
        }
        catch (RemoteException e) {
            Database.get().logout(this.player.getToken());
        }
    }

    void onDisconnection(Player player, int numOfPlayer) {
        try {
            network.onDisconnection(player, numOfPlayer);
        }
        catch (RemoteException e) {
            Database.get().logout(this.player.getToken());
        }
    }

    void onStarting(String map) {
            try {
                network.onStarting(map);
            }
            catch (RemoteException e) {
                Database.get().logout(this.player.getToken());
            }
    }

    void onTimer(int ms) {
            try {
                network.onTimer(ms);
            }
            catch (RemoteException e) {
                Database.get().logout(this.player.getToken());
        }
    }

    public Actor getSelf() {
        return player.getActor();
    }

    public void addNotification(String effectString) {
        this.notificationList.add(effectString);
    }

    /**
     * This is called by the network (acting as a proxy for the client) to provide a view of the
     * game Map
     * @return the view of the gameMap from the point of view of the associated player
     */
    public GameMapView sendMap(){
        return main.getGameMap().generateView(player.getUid());
    }

    public ControllerMessage getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(ControllerMessage currentMessage) {
        this.currentMessage = currentMessage;
        if (!currentMessage.type().equals(SlaveControllerState.WAIT)){
            new Thread(()->{
                try {
                    Thread.sleep(timeoutWindow*(long)1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (this.currentMessage.equals(currentMessage)){
                    //FIXME: gestire il caso in cui il timer scade subito dopo che il client ha
                    // fatto pick per finalizzare l'azione quindi: 1. Dico alla rete di non
                    // accettare più richieste a parte "getInstruction" 2. Aspetto qualche
                    // secondo nel caso avesse appena terminato. 3. Ricontrollo currentMessage e
                    // faccio le mie cose se è uguale

                    //TODO @LORENZO timeout da sistemare qui
                    //network.interrupt();
                    //main.notifyTimeout(this);

                }
            }).start();
        }
    }
}
