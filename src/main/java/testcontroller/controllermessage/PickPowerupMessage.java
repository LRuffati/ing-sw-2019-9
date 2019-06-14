package testcontroller.controllermessage;

import grabbables.PowerUp;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PickPowerupMessage implements ControllerMessage {
    public final SlaveControllerState type;
    private final Function<List<PowerUp>, ControllerMessage> choicesAction;
    private final ChoiceBoard choiceBoard;
    private final boolean optional;
    private final boolean single;

    public PickPowerupMessage(SlaveControllerState state, List<PowerUp> options,
                              Function<List<PowerUp>, ControllerMessage> choicesAction, String description,
                              boolean single){
        this.type = state;
        this.choicesAction = choicesAction;

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
     * This makes the choice
     *
     * @param choices the positions of the chosen elements
     * @return
     */
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        int lenOpts = choiceBoard.getNumOfElems();
        List<PowerUp> powChoices = choices.stream().filter(i-> i>0 & i<lenOpts)
                                    .map(i-> choiceBoard.getPowerUps().get(i))
                                    .collect(Collectors.toList());
        if (!optional & choices.isEmpty()){
            return this;
        } else {
            return choicesAction.apply(powChoices);
        }
    }
}
