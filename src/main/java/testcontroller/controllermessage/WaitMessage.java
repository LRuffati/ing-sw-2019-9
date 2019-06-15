package testcontroller.controllermessage;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;

public class WaitMessage implements ControllerMessage{

    private final Message message;

    public WaitMessage(List<String> notifications){
        // Most WaitMessages have an empty list because it triggers the call of getInstruction in
        // SlaveController, which in turn returns an appropriate message
        this.message = new Message(){
            /**
             * I communicate the changes that happened
             * @return
             */
            @Override
            public List<String> getChanges() {
                return notifications;
            }
        };
    }

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
        return message;
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
     * @return this, since you have to wait
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        if (!message.getChanges().isEmpty())
            return new WaitMessage(List.of());
        else return this;
    }
}
