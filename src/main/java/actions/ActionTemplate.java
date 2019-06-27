package actions;

import actions.effects.EffectTemplate;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import board.Sandbox;
import genericitems.Tuple;
import controller.controllermessage.ControllerMessage;
import org.jetbrains.annotations.NotNull;
import viewclasses.ActionView;

import java.util.*;
import java.util.function.Function;

public class ActionTemplate {

    private final ActionInfo info;

    /**
     * Each element of the Collection is a couple:
     * Second element: The {@link #info#actionId} of another action in the same weapon
     * First element: {@link Boolean#TRUE} if this action must follow the one with ID above
     *                {@link Boolean#FALSE} if it can't follow the action
     */
    private final Collection<Tuple<Boolean, String>> actionRequirements;

    /**
     * Each element of the Collection is a couple:
     * <br/><br/>
     * First element: <ul><li>{@link Boolean#TRUE} if this action requires the above mentioned target
     * to have been acquired already</li>
     *                <li>{@link Boolean#FALSE} if the action requires the target not to
     *                have been acquired (e.g. an optional target being left unassigned)</li></ul>
     * <br/><br/>
     * Second element: The identifier of another target in the same weapon
     */
    private final Collection<Tuple<Boolean, String>> targetRequirements;

    /**
     * The list shows in order which targets will be acquired.
     * The first element of the Tuple is the identifier of the Target
     * The second element is the Template of the Targeter to be used
     */
    private final List<Tuple<String, TargeterTemplate>> targeters;

    /**
     * The effects to be applied, EffectType will be used
     */
    private final List<EffectTemplate> effects;

    public ActionTemplate(ActionInfo info,
                   List<Tuple<String, TargeterTemplate>> targeters, //Order is important
                   List<EffectTemplate> effects){

        this.info = info;

        actionRequirements = new ArrayList<>(info.getActionRequirements());
        targetRequirements = new ArrayList<>(info.getTargetRequirements());

        this.targeters = targeters;
        this.effects = effects;
    }

    public ActionInfo getInfo(){
        return this.info;
    }

    /**
     * This function is provided a list of previously taken {@link #info#actionId} and checks the
     * action represented by the class can be taken
     *
     * @param previousActions A list of string, representing the {@link #info#actionId} of other
     *                        actions in the weapon
     * @return true if the action can be taken, false otherwise
     */
    private boolean verifyActions(Collection<String> previousActions){
        boolean result = true;
        for (Tuple<Boolean, String> i: actionRequirements){
            //TODO: test that contains checks equality, not just reference
            result &= i.x.equals(previousActions.contains(i.y));
            result &= !(i.y.equals(this.info.getActionId())); //Every action just once
        }
        return result;
    }

    /**
     * @see #verifyActions(Collection) verifyActions
     * @param previousTargets a list of strings representing the acquired targets
     * @return true if the action can be taken, false otherwise
     */
    private boolean verifyTargets(Collection<String> previousTargets){
        boolean result = true;
        for (Tuple<Boolean, String> i: targetRequirements){
            //TODO: test that contains checks equality, not just reference
            result &= i.x.equals(previousTargets.contains(i.y));
        }
        return result;
    }

    /**
     * Given an AmmoAmount it checks if it's enough to pay for the usage
     * @param available The ammo available to the user
     * @return true if the given amount is enough to pay
     */
    private boolean verifyCost(@NotNull AmmoAmount available){
        return available.canBuy(info.getCost());
    }

    /**
     *
     * @param existingTargets the targets already selected by the weapon execution
     * @param previousActions the actions preceding this one in this instance of weapon use
     * @param ammoAvailable the ammo available to the player
     * @return true if the action can be executed, false otherwise
     */
    public boolean actionAvailable(Map<String, Targetable> existingTargets,
                                   List<String> previousActions, AmmoAmount ammoAvailable){
        return verifyCost(ammoAvailable) &
                verifyActions(previousActions) &
                verifyTargets(existingTargets.keySet());
    }

    public List<Tuple<String, TargeterTemplate>> getTargeters() {
        return new ArrayList<>(targeters);
    }

    public List<EffectTemplate> getEffects() {
        return new ArrayList<>(effects);
    }

    public Action generate(Sandbox sandbox, Map<String, Targetable> prevTargs,
                           Function<Tuple<Sandbox, Map<String, Targetable>>,
                                   ControllerMessage> finalizer){
        return new Action(sandbox, this, prevTargs, finalizer);
    }



    public ActionView generateView() {
        return new ActionView(info.getName(), info.getActionId(), info.getCost());
    }
}
