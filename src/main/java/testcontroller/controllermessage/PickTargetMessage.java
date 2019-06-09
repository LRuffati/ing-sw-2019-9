package testcontroller.controllermessage;

import actions.targeters.targets.Targetable;
import actions.utils.ChoiceMaker;
import board.Sandbox;
import genericitems.Tuple;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.TargetView;

import java.util.List;
import java.util.function.Function;

public class PickTargetMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Message message;
    private final Function<Integer, ControllerMessage> fun;

    public PickTargetMessage(ChoiceMaker choiceMaker, Message message) {
        options = new ChoiceBoard(choiceMaker, message.message());
        this.message = message;
        fun = choiceMaker::pick;
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
        return this.message;
    }

    /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(int[] choices) {
        if (choices.length < 1){
            return fun.apply(-1);
        } else {
            return fun.apply(choices[0]);
        }
    }
}
