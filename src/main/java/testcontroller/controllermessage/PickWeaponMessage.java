package testcontroller.controllermessage;

import actions.utils.WeaponChooser;
import board.Sandbox;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PickWeaponMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Message message;
    private final Function<List<Integer>, ControllerMessage> fun;
    private final GameMapView sandboxView;


    public PickWeaponMessage(WeaponChooser weaponChooser, String s, Sandbox sandbox) {
        options = new ChoiceBoard(weaponChooser, s);
        fun = weaponChooser::pick;
        message = null;
        sandboxView = sandbox.generateView();
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
     * If the elements refer to a sandbox rather than to the gamemap this returns the correct
     * sandbox, if there is no sandbox involved then null
     *
     * @return
     */
    @Override
    public GameMapView sandboxView() {
        return sandboxView;
    }

    /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        choices =
                choices.stream().filter(i -> i>=0 & i< options.getNumOfElems()).collect(Collectors.toList());
        return fun.apply(choices);
    }
}
