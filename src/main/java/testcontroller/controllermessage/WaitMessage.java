package testcontroller.controllermessage;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

import java.util.List;

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
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return this, since you have to wait
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        return this;
    }
}
