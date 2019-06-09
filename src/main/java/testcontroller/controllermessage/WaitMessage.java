package testcontroller.controllermessage;

import board.GameMap;
import board.Sandbox;
import genericitems.Tuple;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

public class WaitMessage implements ControllerMessage{
    /**
     * This denotes the current state of the player turn
     *
     * @return
     */
    @Override
    public SlaveControllerState type() {
        return SlaveControllerState.WAIT;
    }

    /**
     * This generates the ChoiceBoard
     *
     * @return
     */
    @Override
    public ChoiceBoard genView() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Message getMessage() {
        return null;
    }

    /**
     * Returns the game map linked to this action and the revision at which it had been generated
     *
     * @return
     */
    @Override
    public Tuple<GameMap, Integer> gamemap() {
        return null;
    }

    /**
     * If the elements refer to a sandbox rather than to the gamemap this returns the correct
     * sandbox, if there is no sandbox involved then null
     *
     * @return
     */
    @Override
    public Sandbox sandbox() {
        return null;
    }

    /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(int[] choices) {
        return this;
    }
}
