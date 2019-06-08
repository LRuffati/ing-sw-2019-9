package testcontroller.controllermessage;

import actions.WeaponUse;
import actions.utils.ActionPicker;
import board.Sandbox;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

import java.util.function.Function;

public class PickActionMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Function<Integer, ControllerMessage> fun;

    public PickActionMessage(ActionPicker actionPicker, String s, Sandbox sandbox) {
        options = new ChoiceBoard(actionPicker, s);
        fun = actionPicker::pickAction;

    }

    /**
     * This denotes the current state of the player turn
     *
     * @return
     */
    @Override
    public SlaveControllerState type() {
        return SlaveControllerState.MAIN;
    }

    /**
     * This generates the ChoiceBoard
     *
     * @return
     */
    @Override
    public ChoiceBoard genView() {
        return options;
    }

    /**
     * @return
     */
    @Override
    public Message getMessage() {
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
        if (choices.length==0){
            return fun.apply(-1);
        } else {
            return fun.apply(choices[0]);
        }
    }
}
