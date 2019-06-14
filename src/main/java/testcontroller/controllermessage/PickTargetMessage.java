package testcontroller.controllermessage;

import actions.utils.ChoiceMaker;
import board.Sandbox;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PickTargetMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Function<Integer, ControllerMessage> fun;
    private final Sandbox sandbox;

    public PickTargetMessage(ChoiceMaker choiceMaker, String message, Sandbox sandbox) {
        options = new ChoiceBoard(choiceMaker, message);
        fun = choiceMaker::pick;
        this.sandbox = sandbox;
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
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        choices = choices.stream().filter(i -> i>=0 & i< options.getNumOfElems()).collect(Collectors.toList());
        if (choices.isEmpty()){
            return fun.apply(-1);
        } else {
            return fun.apply(choices.get(0));
        }
    }
}
