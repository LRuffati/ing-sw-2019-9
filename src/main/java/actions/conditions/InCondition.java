package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;

/**
 * Pointlike (in SuperTile)
 */
public class InCondition extends Condition {
    /**
     * True if I'm interested in targets not contained
     */
    private final boolean negated;

    /**
     *
     * @param negated true if I want to check the target is not in the Supertile
     */
    public InCondition(boolean negated){

        this.negated = negated;
    }

    /**
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    public boolean checkTarget(PointLike target, SuperTile checker) {
        return negated ^ checker.containedTiles().contains(target.location());
    }
}
