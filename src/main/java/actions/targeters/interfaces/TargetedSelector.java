package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import board.Sandbox;

/**
 * This interface models the conditions:
 *  + Reachable     targetName TargetedSelector (&amp; [not] reached (min,max) PointLike)
 *  + Distant       targetName TargetedSelector (&amp; [not] distant (min,max) PointLike)
 *  + Contained     targetName TargetedSelector (&amp; [not] in SuperTile)
 */
public interface TargetedSelector extends Targetable {
    /**
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target for which reachableSelector is calculated
     * @param negation whether the condition should be negated or not
     * @return the result of the check
     */
    boolean reachedCondition(Sandbox sandbox, int min, int max, PointLike source, boolean negation);

    /**
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @param logical if true don't go through walls
     * @return the result of the check
     */
    boolean distanceCondition(Sandbox sandbox, int min, int max, PointLike source, boolean negation, boolean logical);

    /**
     * @param container the SuperTile establishing the condition
     * @param negation whether the condition should be negated or not
     * @return the result of the check
     */
    boolean containedSelector(Sandbox sandbox, SuperTile container, boolean negation);
}
