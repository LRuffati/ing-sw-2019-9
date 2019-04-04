package actions.targeters.interfaces;
import java.util.Optional;

/**
 * This interface models the conditions:
 *  + Reachable     targetName TargetedSelector ([not] reached (min,max) PointLike)
 *  + Distant       targetName TargetedSelector ([not] distant (min,max) PointLike)
 *  + Contained     targetName TargetedSelector ([not] in SuperTile)
 */
public interface TargetedSelector {
    /**
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target for which reachable is calculated
     * @param negation whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    Optional<TargetedSelector> reached(int min, int max, PointLike source, boolean negation);

    /**
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    Optional<TargetedSelector> distant(int min, int max, PointLike source, boolean negation);

    /**
     * @param container the SuperTile establishing the condition
     * @param negation whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    Optional<TargetedSelector> contained(SuperTile container, boolean negation);
}
