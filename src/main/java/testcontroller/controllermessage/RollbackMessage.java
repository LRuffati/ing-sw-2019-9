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
            public UpdateTypes type() {
                return UpdateTypes.DESCRIPTION;
            }

            @Override
            public String message() {
                return "Invalid state, roll back one or more actions";
            }

            @Override
            public List<EffectView> getChanges() {
                return null;
            }

            @Override
            public Sandbox sandbox() {
                return null;
            }

            @Override
            public Integer gameMapIteration() {
                return null;
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
    public ControllerMessage pick(List<Integer> choices) {
        return this;
    }
}
