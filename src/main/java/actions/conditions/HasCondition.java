package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;

/**
 * SuperTile (has PointLike)
 */
public class HasCondition extends Condition {
    /**
     * If I'm looking for Supertiles not containing the target
     */
    private final boolean negated;

    /**
     *
     * @param negated true if I'm interested in targets not containing something
     */
    public HasCondition(boolean negated){

        this.negated = negated;
    }

    /**
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    public boolean checkTarget(SuperTile target, PointLike checker) {
        return negated ^ target.containedTiles().contains(checker.location());
    }
}
