package testcontroller.controllermessage;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;

/**
 * This class represents every message guiding the evolution of the game.
 * There are two types, interactive messages require a choice to be made by the player, update
 * messages are requests made by the server to the client to update its internal status.
 * Parallel to this there is an update system to keep the client apprised to the status changes
 * in the server. These don't require an interaction by the user but are all the same need to
 *
 * The class must be able to:
 *      1. Present a choice to the user
 *      2. Instruct the client controller to update the view or status according to the message
 */
public interface ControllerMessage {
    /**
     * This denotes the current state of the player turn
     * @return
     */
    SlaveControllerState type();

    /**
     * This generates the ChoiceBoard
     * @return
     */
    ChoiceBoard genView();

    /**
     *
     * @return
     */
    Message getMessage();

    /**
     * Returns the hash of the game map linked to this action and the revision at which it had been generated
     * @return
     */
    String gamemap();

    /**
     * If the elements refer to a sandbox rather than to the gamemap this returns the correct
     * sandbox, if there is no sandbox involved then null
     * @return
     */
    GameMapView sandbox();

    /**
     * This makes the choice
     * @param choices the positions of the chosen elements
     * @return
     */
    ControllerMessage pick(List<Integer> choices);

}
