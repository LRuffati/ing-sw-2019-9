package testcontroller.controllermessage;

import actions.utils.WeaponChooser;
import board.Sandbox;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

import java.util.List;
import java.util.function.Function;

public class PickWeaponMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Message message;
    private final Function<int[], ControllerMessage> fun;


    public PickWeaponMessage(WeaponChooser weaponChooser, String s, Sandbox sandbox) {
        options = new ChoiceBoard(weaponChooser, s);
        fun = weaponChooser::pick;
        message = null;
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
        return fun.apply(choices);
    }
}
