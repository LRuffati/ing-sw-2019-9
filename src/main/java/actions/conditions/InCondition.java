package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;

/**
 * Pointlike (in SuperTile)
 */
public class InCondition extends Condition {
    private final boolean negated;

    InCondition(boolean negated){

        this.negated = negated;
    }

    boolean checkTarget(PointLike target, SuperTile checker) {
        return negated ^ checker.containedTiles().contains(target.location());
    }
}
