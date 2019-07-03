package controller.controllermessage;

import grabbables.PowerUp;
import controller.ChoiceBoard;
import controller.Message;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is an implementation of {@link ControllerMessage ControllerMessage}
 * and is used when a powerUp can be chosen
 */
public class PickPowerupMessage implements ControllerMessage {
    public final SlaveControllerState type;
    private final Function<List<PowerUp>, ControllerMessage> choicesAction;
    private final ChoiceBoard choiceBoard;
    private final boolean optional;
    private final boolean single;
    private final List<String> message;
    private final List<PowerUp> originalPowup;

    public PickPowerupMessage(SlaveControllerState state, List<PowerUp> options,
                              Function<List<PowerUp>, ControllerMessage> choicesAction, String description,
                              boolean single, List<String> notifications){
        this.type = state;
        this.choicesAction = choicesAction;
        this.message = notifications;
        this.originalPowup = List.copyOf(options);

        switch (state){
            case RESPAWN:
                this.choiceBoard = ChoiceBoard.powupChoiceFactory(options, description, true, false);
                this.optional = false;
                this.single = true;
                break;
            case MAIN:
                this.choiceBoard = ChoiceBoard.powupChoiceFactory(options, description, single,
                        true);
                this.optional = true;
                this.single = single;
                break;
            case USETAGBACK:
                this.choiceBoard = ChoiceBoard.powupChoiceFactory(options, description, true, true);
                this.optional = true;
                this.single = true;
                break;
            default:
                this.choiceBoard = null;
                this.single = true;
                this.optional = true;
        }
    }

    /**
     * Only to pay
     * @param options
     */
    public PickPowerupMessage(List<PowerUp> options,
                              Function<List<PowerUp>, ControllerMessage> choicesAction,
                              boolean optional,
                              ChoiceBoard choiceBoard){
        this.choicesAction = choicesAction;
        this.optional = optional;
        this.choiceBoard = choiceBoard;
        type = SlaveControllerState.MAIN;
        single = false;
        message = List.of();
        originalPowup = options;
    }

    /**
     * This denotes the current state of the player turn
     *
     * @return
     */
    @Override
    public SlaveControllerState type() {
        return type;
    }

    /**
     * This generates the ChoiceBoard
     *
     * @return
     */
    @Override
    public ChoiceBoard genView() {
        return this.choiceBoard;
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
        return null; // Powerups are never chosen from within a sandbox
    }

    /**
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        int lenOpts = choiceBoard.getNumOfElems();
        List<PowerUp> powChoices = choices.stream()
                        .distinct()
                        .filter(i-> i>=0 && i<lenOpts)
                                    .map(originalPowup::get)
                                    .collect(Collectors.toList());
        if (!optional && choices.isEmpty()){
            return this;
        } else {
            return choicesAction.apply(powChoices);
        }
    }
}
