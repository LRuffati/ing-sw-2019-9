package controller.controllermessage;

import actions.utils.ChoiceMaker;
import board.Sandbox;
import controller.ChoiceBoard;
import controller.Message;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is an implementation of {@link ControllerMessage ControllerMessage}
 * and is used when a target can be chosen
 */
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
     * Used for targets with a description embedded
     * @param choiceMaker
     * @param sandbox
     */
    public PickTargetMessage(ChoiceMaker choiceMaker, Sandbox sandbox){
        String descr = choiceMaker.getDescription();
        if (descr.length()==0)
            descr = "Scegli un bersaglio";
        options = new ChoiceBoard(choiceMaker, descr);
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
     * @return
     */
    @Override
    public Message getMessage() {
        return new Message() {
            @Override
            public List<String> getChanges() {
                return List.of();
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
        return sandbox.generateView();
    }

    /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        choices = choices.stream()
                        .distinct()
                        .filter(i -> i>=0 && i< options.getNumOfElems()).collect(Collectors.toList());
        if (choices.isEmpty()){
            return fun.apply(-1);
        } else {
            return fun.apply(choices.get(0));
        }
    }
}
