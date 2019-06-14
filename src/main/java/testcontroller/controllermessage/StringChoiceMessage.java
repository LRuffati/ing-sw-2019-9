package testcontroller.controllermessage;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;

import java.util.List;
import java.util.function.Function;

public class StringChoiceMessage implements ControllerMessage {

    private final String description;
    private final Function<Integer, ControllerMessage> picker;
    private List<String> options;

    public StringChoiceMessage(List<String> options, String description, Function<Integer,
            ControllerMessage> picker){
        this.options = options;
        this.description = description;
        this.picker = picker;
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
        choices =
                choices.stream().filter(i -> i>=0 & i<options.size()).collect(Collectors.toList());
        if (choices.isEmpty()) {
            return this;
        } else {
            return picker.apply(choices.get(0));
        }
    }
}
