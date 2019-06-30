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
        for (Tuple<Boolean, String> i: info.getActionRequirements()){
            //TODO: test that contains checks equality, not just reference
            result &= i.x.equals(previousActions.contains(i.y));
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
        for (Tuple<Boolean, String> i: info.getTargetRequirements()){
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
