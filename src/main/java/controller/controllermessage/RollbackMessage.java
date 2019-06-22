package controller.controllermessage;

import controller.ChoiceBoard;
import controller.Message;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;

public class RollbackMessage implements ControllerMessage {
    private final List<String> message;

    public RollbackMessage(String message) {
        //This class only notifies an error, needs only and a message to show
        this.message = List.of(message);
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
        return new Message() {
            @Override
            public List<String> getChanges() {
                return message;
            }
        };
    }

    /**
     * If the elements refer to a sandbox rather than to the gamemap this returns the correct
     * sandbox, if there is no sandbox involved then null
     *
     * @return
     */
    @Override
    public GameMapView sandboxView() {
        return null;
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
