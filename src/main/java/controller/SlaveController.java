package controller;

import actions.ActionBundle;
import actions.ActionTemplate;
import actions.effects.Effect;
import actions.effects.PayTemplate;
import actions.effects.ReloadTemplate;
import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import actions.utils.*;
import board.Sandbox;
import genericitems.Tuple;
import grabbables.PowerUp;
import network.Database;
import grabbables.Weapon;
import network.Player;
import network.ServerInterface;
import player.Actor;
import controller.controllermessage.*;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
    public ReentrantLock lockMessageSet;

    public SlaveController(MainController main, Player player, ServerInterface network) {
        this.player = player;
        this.network = network;
        this.setCurrentMessage(new WaitMessage(List.of()));
        this.main = main;
        this.timeoutWindow = main.timeoutTime;
        notificationList = new ArrayList<>();
        lockMessageSet = new ReentrantLock();
    }

    /**
     * This function sets in motion the main turn line
     */
    public void startMainAction(){
        if (!player.isOnLine()) {
            main.endTurn(getSelf());
            return;
        }

        this.onTimeout = () -> main.endTurn(getSelf());
        this.setCurrentMessage(setPowUps(new ArrayList<>(), getSelf().getActions())); //
        // setPowUps include il proxy
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

        // Check that I can pay the targeting scope
        if (getSelf().getPowerUp().size()==1 && AmmoAmountUncapped.zeroAmmo.canBuy(getSelf().getAmmo())){
            pows = pows.stream()
                    .filter(p -> p.getType().equals(PowerUpType.TARGETINGSCOPE))
                    .collect(Collectors.toList());
        }

        Sandbox sandbox = getSelf().getGm().createSandbox(getSelf().pawnID());

        SetMessageProxy proxy = new SetMessageProxy(this);

        Function<Sandbox, ControllerMessage> reloadMerger = // Will take the effects in
                // sandbox and
                sandbox1 -> { // merge them into MainController
                    if (!proxy.setControllerMessage(new WaitMessage(List.of()))){
                        return new WaitMessage(List.of());
                    }

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
                    if (!proxy.setControllerMessage(new WaitMessage(List.of()))){
                        return new WaitMessage(List.of());
                    }

                    Runnable onResolved = () -> this.setCurrentMessage(this.setPowUps(effectList, bundlesTail));
                    // setPowUps include il proxy

                    new Thread(() -> main.resolveEffect(this, effectList, onResolved)).start();

                    return new WaitMessage(List.of());
                };

        List<List<ActionTemplate>> tail = nextActs.subList(Math.min(1,nextActs.size()), nextActs.size());

        ControllerMessage ret;

        if ((pows.isEmpty()) && (nextActs.isEmpty())){ // No powUp available, only reload left
            ret = reloadMessage;

        } else if ((pows.isEmpty()) && !nextActs.isEmpty()){ // No powerups but ActionBundle
            // available
            ActionBundle action = new ActionBundle(sandbox, nextActs.get(0), bundleFinalizer.apply(tail));
            ret = new PickActionMessage(action, "Scegli un'azione",
                sandbox, getNotifications());

        } else { // Powerups
            Function<List<PowerUp>, ControllerMessage> onPowupPick =
                    list -> {
                        if (list.isEmpty()) {
                            if (nextActs.isEmpty()) {
                                return reloadMessage;
                            } else {
                                ActionBundle action = new ActionBundle(sandbox, nextActs.get(0), bundleFinalizer.apply(tail));
                                return new PickActionMessage(action, "Scegli un'azione", sandbox,
                                        getNotifications());
                            }
                        } else {
                            //3. Call this function with same params
                            Runnable onApplied = () -> {
                                        this.setCurrentMessage(this.setPowUps(lastEffects, nextActs));
                                    };
                            //2. Apply powerup
                            return list.get(0).usePowup(proxy, lastEffects, onApplied);
                        }
                    };
            ret = new PickPowerupMessage(SlaveControllerState.MAIN, pows, onPowupPick,
                    "Scegli un powerup da usare", true, getNotifications());
        }
        proxy.setMessage(ret);
        return ret;
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

        SetMessageProxy proxy = new SetMessageProxy(this);

        Function<List<PowerUp>, ControllerMessage> onPick =
                list -> {
                    if (proxy.setControllerMessage(new WaitMessage(List.of()))){
                        new Thread(()-> onRespawned.accept(list.get(0))).start();
                    }
                    return new WaitMessage(List.of());
                };

        if (!player.isOnLine()) {
            onRespawned.accept(powups.get(0));
            return;
        }
        // If timeout is triggered I should spawn in a random location
        // onRespawned is blocking so I should start it in a thread
        this.onTimeout = () -> onRespawned.accept(powups.get(0));

        ControllerMessage msg = new PickPowerupMessage(
                SlaveControllerState.RESPAWN,
                powups,
                onPick,
                "Scegli lo spawn point",
                true,
                List.of()
                );
        proxy.setMessage(msg);
        this.setCurrentMessage(msg);
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

        /*
        1. Check if you have any available tagbacks
            a. No, run on finished with empty optional
        2. Yes, make user choose which powerup to use, if any (set ControllerMessage)
            a. None, return a waitMessage and call onFinished with empty
        3. return a waitMessage to the user and call on finished with the powerup to use
         */

        Sandbox sandbox = getSelf().getGm().createSandbox(getSelf().pawnID());
        BasicTarget other = sandbox.getBasic(offender.pawnID());
        SetMessageProxy proxy = new SetMessageProxy(this);

        if (other.seen(sandbox, sandbox.getBasic(getSelf().pawnID()),true)){ // 1a
            new Thread(()->onFinished.accept(Optional.empty())).start();
            return;
        }

        List<PowerUp> tagbacks = getSelf().getPowerUp().stream()
                .filter(powerUp -> powerUp.getType().equals(PowerUpType.TAGBACKGRANADE))
                .collect(Collectors.toList());

        if (tagbacks.isEmpty()){ // 1a
            new Thread(()->onFinished.accept(Optional.empty())).start();
            return;
        }


        /*
         * Will be called by the controllerMessage, should put both the slave (by putMessage) and
         *  the client (by return) in wait
         */
        Function<List<PowerUp>, ControllerMessage> powUpPickerAccept = lista -> {
            if (proxy.setControllerMessage(new WaitMessage(List.of()))) {
                if (lista.isEmpty()) {
                    new Thread(() -> onFinished.accept(Optional.empty())).start(); //2a
                } else {
                    new Thread(() -> onFinished.accept(Optional.of(lista.get(0)))).start();
                }
            }
            return new WaitMessage(List.of());
        };

        ControllerMessage askWhichTagBack =
                new PickPowerupMessage(
                        SlaveControllerState.USETAGBACK,
                        tagbacks,
                        powUpPickerAccept,
                        "Scegli quale granata venom usare",
                        true,
                        List.of()
                );

        // Scegli il target
        ChoiceMaker targetConfirmation = new ChoiceMaker() {
            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                return new Tuple<>(true, List.of(other.generateView(sandbox)));
            }

            @Override
            public ControllerMessage pick(int choice) {
                if (choice==0){
                    return askWhichTagBack;
                } else {
                    if (proxy.setControllerMessage(new WaitMessage(List.of()))) {
                        new Thread(() -> onFinished.accept(Optional.empty())).start();
                    }
                    return new WaitMessage(List.of());
                }
            }
        };

        ControllerMessage askTargetConfirmation = new PickTargetMessage(
                targetConfirmation,
                "Vuoi dare un marchio a questo giocatore?",
                sandbox);

        if (!player.isOnLine()){
            onFinished.accept(Optional.empty());
            return;
        }
        this.onTimeout = () -> onFinished.accept(Optional.empty());
        proxy.setMessage(askTargetConfirmation);
        this.setCurrentMessage(askTargetConfirmation);
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
    public synchronized ControllerMessage getInstruction(){
        ControllerMessage mess = getCurrentMessage();
        if (mess.type().equals(SlaveControllerState.WAIT)) {
            List<String> old = new ArrayList<>(mess.getMessage().getChanges());
            old.addAll(getNotifications());
            setCurrentMessage(new WaitMessage(List.of()));
            return new WaitMessage(old);
        }

        else return mess;
    }

    /**
     * Method used to notify the client that a new player just joined the game
     */
    void onConnection(Player player, int numOfPlayer) {
        try {
            network.onConnection(player, numOfPlayer);
        }
        catch (RemoteException e) {
            Database.get().logout(this.player.getToken());
        }
    }

    /**
     * Method used to notify the client that a new player just left the game
     */
    void onDisconnection(Player player, int numOfPlayer) {
        try {
            network.onDisconnection(player, numOfPlayer);
        }
        catch (RemoteException e) {
            Database.get().logout(this.player.getToken());
        }
    }

    /**
     * Method used to notify the client that the game is starting
     */
    void onStarting(String map, GameMode gameMode) {
            try {
                network.onStarting(map, gameMode);
            }
            catch (RemoteException e) {
                Database.get().logout(this.player.getToken());
            }
    }

    /**
     * Method used to notify the client that a new timer is starting
     */
    void onTimer(int ms) {
            try {
                network.onTimer(ms);
            }
            catch (RemoteException e) {
                Database.get().logout(this.player.getToken());
        }
    }

    /**
     * Method used to notify the client that the game is finished, it also contains the winner of the game
     */
    void onWinner(String winner, int winnerPoints) {
        try {
            network.onWinner(winner, winnerPoints, player.getActor().getPoints());
        }
        catch (RemoteException e) {
            Database.get().logout(this.player.getToken());
        }
    }

    public boolean isOnline() {
        return player.isOnLine();
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

    public synchronized void setCurrentMessage(ControllerMessage nextMessage) {
        if (!nextMessage.type().equals(SlaveControllerState.WAIT)){
            this.currentMessage = nextMessage;
            TimerTask task = new TimerTask() {
                public void run() {
                    if (SlaveController.this.lockMessageSet.tryLock()){
                        if (SlaveController.this.currentMessage==currentMessage){
                            //TODO: di alla rete di pulire la sua memoria
                            //TODO: main notify timeout (shouldn't do anything, just send a
                            // notification that the player went in timeout)
                            SlaveController.this.setCurrentMessage(new WaitMessage(List.of()));
                            new Thread(SlaveController.this.onTimeout).start();
                        }
                        SlaveController.this.lockMessageSet.unlock();
                    }
                    return; // If there's a lock something is about to put a message in
                }
            };
            Timer timer = new Timer("Timer");
            timer.schedule(task, timeoutWindow*1000);
        } else { //TODO merge old and new wait messages
            if (this.currentMessage.type().equals(SlaveControllerState.WAIT)){
                List<String> old = new ArrayList<>(this.currentMessage.getMessage().getChanges());
                old.addAll(nextMessage.getMessage().getChanges());
                this.currentMessage = new WaitMessage(old);
            } else {
                this.currentMessage = nextMessage
            }
        }
    }

    /**
     * Called when I have to choose between multiple weapons to pick and possibly drop one
     */
    public void makeGrabChoice(Set<Weapon> options, BiConsumer<Weapon, Optional<Weapon>> onChoice){

        Sandbox sandbox = main.getGameMap().createSandbox(getSelf().pawnID());
        /*
        1. Show only weapons you can buy
        2. If you have three already pick one to drop
         */
        AmmoAmountUncapped funds = sandbox.getUpdatedTotalAmmoAvailable();
        List<Weapon> buyable =
                options.stream()
                        .filter(weapon -> funds.canBuy(weapon.getBuyCost()))
                        .collect(Collectors.toList());
        List<Weapon> owned =
                sandbox.getArsenal().stream()
                        .map(tup -> tup.y)
                        .collect(Collectors.toList());

        boolean haveToDrop =
                (getSelf().getLoadedWeapon().size()+getSelf().getUnloadedWeapon().size()) >= 3;

        SetMessageProxy proxy = new SetMessageProxy(this);

        BiFunction<Weapon, Sandbox, ControllerMessage>  afterPay = (weapPicked, sandPay) -> {
            if (haveToDrop){
                // Choose weapon to drop
                // Apply effects
                // Call the biconsumer
                WeaponChooser weaponToDrop = new WeaponChooser() {
                    @Override
                    public Tuple<Boolean, Boolean> params() {
                        return new Tuple<>(false,true);
                    }

                    @Override
                    public List<Weapon> showOptions() {
                        return owned;
                    }

                    @Override
                    public ControllerMessage pick(List<Integer> choice) {
                        Optional<Weapon> toDrop = Optional.of(owned.get(choice.get(0)));

                        Runnable onRes = () -> onChoice.accept(weapPicked, toDrop);

                        new Thread(()-> main.resolveEffect(
                                SlaveController.this,
                                sandPay.getEffectsHistory(),
                                onRes)).start();

                        return new WaitMessage(List.of());
                    }
                };
                return new PickWeaponMessage(
                        weaponToDrop,
                        "Scegli che arma lasciare",
                        sandPay);
            } else {
                if (proxy.setControllerMessage(new WaitMessage(List.of()))) {
                    Runnable onRes = () -> onChoice.accept(weapPicked, Optional.empty());
                    new Thread(() -> main.resolveEffect(
                            SlaveController.this,
                            sandPay.getEffectsHistory(),
                            onRes)).start();
                }
                return new WaitMessage(List.of());
            }
        };

        WeaponChooser weaponToPick = new WeaponChooser() {
            @Override
            public Tuple<Boolean, Boolean> params() {
                return new Tuple<>(false, true);
            }

            @Override
            public List<Weapon> showOptions() {
                return buyable;
            }

            @Override
            public ControllerMessage pick(List<Integer> choice) {
                AmmoAmount zero = new AmmoAmount();
                Weapon weaponToGrab = buyable.get(choice.get(0));
                AmmoAmount cost = weaponToGrab.getBuyCost();
                if (!zero.canBuy(cost)){ // if the cost is not 0
                    PayTemplate payTemplate = new PayTemplate(cost);
                    return payTemplate.spawn(
                            Map.of(),
                            sandbox,
                            sandPay -> afterPay.apply(weaponToGrab, sandPay));
                } else {
                    return afterPay.apply(weaponToGrab, sandbox);
                }
            }
        };

        ControllerMessage pickWeaponToGrab = new PickWeaponMessage(
                weaponToPick,
                "Scegli che arma raccogliere",
                sandbox);

        proxy.setMessage(pickWeaponToGrab);

        this.setCurrentMessage(pickWeaponToGrab);
    }
}
