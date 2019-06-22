package controller.externalinterfaces;

import actions.ActionTemplate;

import java.util.List;

public interface ActorIface {
    /**
     *
     * @return the actions available to the user. Each list of templates will generate an
     * actionBundle
     */
    List<List<ActionTemplate>> getActions();

    /**
     * Signals to the actor that the final frenzy has begun. If the player has not been damaged
     * it will change the actions available right away, otherwise on the first death
     * @param beforeFirst if the actor comes after the first player in the game order
     */
    void frenzyStarted(boolean beforeFirst);
}
