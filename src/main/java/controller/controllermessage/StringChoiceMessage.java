package controller.controllermessage;

import board.Sandbox;
import controller.ChoiceBoard;
import controller.Message;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringChoiceMessage implements ControllerMessage {

    private final String description;
    private final Function<Integer, ControllerMessage> picker;
    private List<String> options;
    private final GameMapView sandboxView;

    public StringChoiceMessage(List<String> options, String description, Function<Integer,
            ControllerMessage> picker, Sandbox sandbox){
        this.options = options;
        this.description = description;
        this.picker = picker;
        this.sandboxView = sandbox.generateView();
    }

    /**
     * This denotes the current state of the player turn
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
        return new ChoiceBoard(options, description);
    }

    /**
     * @return
     */
    @Override
    public Message getMessage() {
        return new Message() {
            @Override
            public List<String> getChanges() {
                return List.of(); // No change should have happened, if it did I will show on the
                // next wait message
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
                        .filter(i -> i>=0 & i<options.size()).collect(Collectors.toList());
        if (choices.isEmpty()) {
            return this;
        } else {
            return picker.apply(choices.get(0));
        }
    }
}
