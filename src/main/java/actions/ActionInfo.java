package actions;

import actions.utils.AmmoAmount;
import genericitems.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * This is the basic action information
 * It tells how much it costs to play it, which targets are needed/forbidden, ditto for the
 * previous actions having been played.
 * It also provides information such as name, unique identifier, if the action is bound to
 * another action (contemporaneous actions) and if an action should be represented or not in the
 * view
 */
public class ActionInfo {
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

    public ActionInfo (String name, String actionId, @NotNull AmmoAmount cost,
                Collection<Tuple<Boolean, String>> actionRequirements,
                Collection<Tuple<Boolean, String>> targetRequirements,
                Optional<String> masterAction, boolean show){

        this.name = name;
        this.actionId = actionId;
        this.cost = cost;
        this.actionRequirements = actionRequirements;
        this.targetRequirements = targetRequirements;
        this.masterAction = masterAction;
        this.show = show;
    }

    public String getName() {
        //Strings are immutable so it's safe to provide the reference
        return name;
    }

    public boolean isShow() {
        return show;
    }

    public String getActionId() {
        //Strings are immutable so it's safe to provide the reference
        return actionId;
    }

    public AmmoAmount getCost() {
        // So long as AmmoAmount is immutable I can return
        return cost;
    }

    public Collection<Tuple<Boolean, String>> getActionRequirements() {
        // Tuple is immutable, so I can not worry about doing a deep copy
        return actionRequirements;
    }

    public Collection<Tuple<Boolean, String>> getTargetRequirements() {
        // Tuple is immutable, so I can not worry about doing a deep copy
        return targetRequirements;
    }

    public Optional<String> getMasterAction() {
        // Tuple is immutable, so I can not worry about doing a deep copy
        return masterAction;
    }


}
