package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;

/**
 * SuperTile (has PointLike)
 */
public class HasCondition extends Condition {

    private final boolean negated;

    HasCondition(boolean negated){

        this.negated = negated;
    }

    boolean checkTarget(SuperTile target, PointLike checker) {
        return negated ^ target.containedTiles().contains(checker.location());
    }
}
