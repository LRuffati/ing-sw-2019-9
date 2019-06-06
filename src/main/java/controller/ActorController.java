package controller;


import actions.ActionBundle;
import actions.ActionTemplate;
import actions.effects.Effect;
import controllerresults.ControllerActionResultServer;
import network.Player;
import player.Actor;

import java.util.List;

/**
 * This class represents the controller of the single player, it is accessed
 */
public class ActorController {

    /**
     * The actor this controller controls
     */
    final Actor actor;

    /**
     * This stores the action bundle in use during an actionSequence
     */
    private ActionBundle currentBundle = null;

    public ActorController(Player player){
        actor = player.getActor();
    }

    ControllerActionResultServer startActionSequence(){
        if (currentBundle == null) {
            List<ActionTemplate> actions = actor.getActions();
            currentBundle = new ActionBundle(actor.getGm(), actions, actor.pawnID());
        }
        return new ControllerActionResultServer(
                currentBundle,
                "Scegli che mossa vuoi fare",
                actor.getGm().createSandbox(actor.pawnID())
        );
    }

    ControllerMainLineResultServer mergeActions(){
        if (currentBundle == null){
            return new ControllerMainLineResultServer(MainResultType.NOBUNDLE);
        }
        if (!currentBundle.isFinalized()){
            return new ControllerMainLineResultServer(MainResultType.FINISHBUNDLE);
        }
        List<Effect> effectsToMerge = currentBundle.getEffects();




    }
}
