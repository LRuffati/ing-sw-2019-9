package testcontroller.controllermessage;

import board.GameMap;
import board.Sandbox;
import genericitems.Tuple;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import testcontroller.controllerstates.UpdateTypes;

import java.util.List;

public class RollbackMessage implements ControllerMessage {
    public RollbackMessage(String message) {
    }

    /**
     * This denotes the current state of the player turn
     *
     * @return
     */
    @Override
    public SlaveControllerState type() {
        return SlaveControllerState.ROLLBACK;
    }

   /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        return this;
    }
}
