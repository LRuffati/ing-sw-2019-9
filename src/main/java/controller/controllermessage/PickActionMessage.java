package controller.controllermessage;

import actions.utils.ActionPicker;
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
 * and is used when an action can be chosen
 */
public class PickActionMessage implements ControllerMessage{

    private final ChoiceBoard options;
    private final Function<Integer, ControllerMessage> fun;
    private final List<String> message;
    private final GameMapView sandboxView;

    public PickActionMessage(ActionPicker actionPicker, String s, Sandbox sandbox,
                             List<String> notifications) {
        options = new ChoiceBoard(actionPicker, s);
        fun = actionPicker::pickAction;
        this.message = notifications;
        this.sandboxView = sandbox.generateView();
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
                choices.stream()
                        .distinct()
                        .filter(i -> i>=0 & i< options.getNumOfElems()).collect(Collectors.toList());
        if (choices.isEmpty()){
            return fun.apply(-1);
        } else {
            return fun.apply(choices.get(0));
        }
    }
}
