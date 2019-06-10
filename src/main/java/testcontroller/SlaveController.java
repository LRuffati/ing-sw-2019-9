package testcontroller;

import actions.Action;
import actions.ActionBundle;
import actions.effects.Effect;
import grabbables.PowerUp;
import network.Player;
import network.ServerInterface;
import player.Actor;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.PickPowerupMessage;
import testcontroller.controllermessage.WaitMessage;

import java.awt.*;
import java.util.List;
import java.util.Set;
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
    public final Actor self;


    public SlaveController(Player player, ServerInterface network) {
        this.player = player;
        this.network = network;
        this.currentMessage = new WaitMessage();
        this.self = player.getActor();
    }

    /**
     * This function sets in motion the main turn line
     *
     * @param onEndTurn The action to perform once I don't have anything else to do
     * @return
     */
    public boolean startMainAction(Runnable onEndTurn){
        List<ActionBundle> turnActions = self.getActions(); //Non include il reload
        this.currentMessage = setPowUps(List.of(), turnActions);
        return true;
    }

    /**
     * The finalizer of an ActionBundle or start main action calls this function and sets the
     * slave controller message to the result.
     * The result makes you pick powerups, then resolves them and the finalizer returns a
     * PickActionMessage(nextActs.get(0)), if nextActs is empty then it starts the reload instead
     *
     * @param lastEffects
     * @param nextActs
     * @return
     */
    protected ControllerMessage setPowUps(List<Effect> lastEffects, List<ActionBundle> nextActs){
        List<PowerUp> pows = self.getPowerUp().stream()
                                    .filter(i->i.canUse(lastEffects))
                                    .collect(Collectors.toList());

        /*TODO: create finalizer for the powerups, from last to first (if nextacts isn't empty):
                n) Put thread in actionBundle and sets slaveController to pickActionMessage(actionb)
                n-1) Create thread with the final action of calling setPowerUps with the tail of
                nextActs and the effects
                n-2) Apply effects to the Main and passes last two actions as a finalizer
                ...
                2) Returns a pickTarget for the second
                1) Returns a pickTarget for the first powerup
        * */
        new PickPowerupMessage(pows, )
    }

    /**
     * This function is invoked when the player needs to respawn
     *
     * @param cardstotake the number of powerups that you need to pickup, on the first round it
     *                    should be two, on subsequent rounds 1
     * @return
     */
    public boolean startRespawn(int cardstotake, Runnable onRespawned){
        return false;
    }

    /**
     * This method is called when a player is damaged by another player and has one or more
     * takeback grenades in his stack
     *
     * @param offender The player which caused the damage
     * @return
     */
    public boolean startTagback(Actor offender, Runnable onFinished){
        return false;
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
        return null;
    }



    void onConnection(Player player) {
        network.onConnection(player);
    }

    void onDisconnection(Player player) {
        network.onDisconnection(player);
    }

    void onStarting(String map) {
        network.onStarting(map);
    }

    void onTimer(int ms) {
        network.onTimer(ms);
    }
}
