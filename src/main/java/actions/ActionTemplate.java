package actions;

import actions.effects.EffectTemplate;
import actions.effects.EffectType;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import board.Sandbox;
import genericitems.Tuple;

import java.util.*;

public class ActionTemplate {

    /**
     * The name to display, this and {@link #show} might be substituted by an ad-hoc
     * object depending on the view being used
     */
    private final String name;

    /**
     * Whether the action should be displayed or not.
     * Use case: when an action described as an unitary entity in natural language actually
     * represents two distinct actions in the formal specification, e.g. Mitragliatrice
     */
    private final boolean show;

    /**
     * A machine friendly code to distinguish actions
     */
    private final String actionId;

    /**
     * The cost to be paid for an action to be performed
     */
    private final AmmoAmount cost;

    /**
     * Each element of the Collection is a couple:
     * Second element: The {@link #actionId} of another action in the same weapon
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
     * For contemporaneous actions an action will be identified as master (the first one) and all
     * others will reference it in this variable.<br/>
     * The constructor for the class should make sure the tuple {X = TRUE, Y=masterAction}
     * appears in {@link #actionRequirements}<br/>
     * After the master action will have been performed the user will be presented with all
     * contemporaneous actions as the next option and if he doesn't choose any will be shown
     * other actions normally.<br/>
     * <br/>
     * These actions will merge their effects by applying a single move, mark and damage order
     */
    private final Optional<String> masterAction;

    /**
     * The list shows in order which targets will be acquired.
     * The first element of the Tuple is the identifier of the Target
     * The second element is the Template of the Targeter to be used
     */
    private final List<Tuple<String, TargeterTemplate>> targeters;

    /**
     * The effects to be applied, EffectType will be used
     */
    private final Map<EffectType, Collection<EffectTemplate>> effects;

    public ActionTemplate(ActionInfo info,
                   List<Tuple<String, TargeterTemplate>> targeters, //Order is important
                   Map<EffectType, Collection<EffectTemplate>> effects){

        name = info.getName();
        show = info.isShow();
        actionId = info.getActionId();
        cost = info.getCost();
        actionRequirements = new ArrayList<>(info.getActionRequirements());
        targetRequirements = new ArrayList<>(info.getTargetRequirements());
        masterAction = info.getMasterAction();


        this.targeters = targeters;
        this.effects = effects;
    }

    /**
     * This function is provided a list of previously taken {@link #actionId} and checks the
     * action represented by the class can be taken
     *
     * @param previousActions A list of string, representing the {@link #actionId} of other
     *                        actions in the weapon
     * @return true if the action can be taken, false otherwise
     */
    private boolean verifyActions(Collection<String> previousActions){
        boolean result = true;
        for (Tuple<Boolean, String> i: actionRequirements){
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
    private boolean verifyCost(AmmoAmount available){
        return available.compareTo(cost)>=0;
    }

    /**
     *
     * @param existingTargets the targets already selected by the weapon execution
     * @param previousActions the actions preceding this one in this instance of weapon use
     * @param ammoAvailable the ammo available to the player
     * @return true if the action can be executed, false otherwise
     */
    private boolean actionAvailable(Map<String, Targetable> existingTargets,
                                   List<String> previousActions, AmmoAmount ammoAvailable){
        return verifyCost(ammoAvailable) &
                verifyActions(previousActions) &
                verifyTargets(existingTargets.keySet());
    }

    /**
     * This is the creator for the concrete Action, it is the point of contact for the creation
     * of the Action
     * @param gameStatus the sandbox connected to the weapon use
     * @param existingTargets the targets previously selected
     * @param previousActions the actions previously executed
     * @param ammoAvailable the ammo available to the player
     * @return an empty optional if the action can't be executed, a pair containing the Ammo left
     * after the execution and the concrete action if it can be executed
     */
    public Optional<Tuple<AmmoAmount, Action>> spawn(Sandbox gameStatus,
                                         Map<String, Targetable> existingTargets,
                                                     List<String> previousActions,
                                                     AmmoAmount ammoAvailable){
        if (actionAvailable(existingTargets, previousActions, ammoAvailable)) {
            Action action = new Action(gameStatus, this.masterAction, actionId, targeters,
                    effects, existingTargets);
            AmmoAmount newAmount = ammoAvailable.subtract(cost);
            return Optional.of( new Tuple<>(newAmount, action));
        } else return Optional.empty();
    }
}
