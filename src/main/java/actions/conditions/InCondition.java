package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;

/**
 * Checks that the target is contained in the given source
 */
public class InCondition implements Condition {
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
     * @param target has to be a pointlike
     * @param checker has to be a SuperTile
     */
    @Override
    public boolean checkTarget(Sandbox sandbox, Targetable target, Targetable checker) {
        if (!(target instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike target");
        }
        if (!(checker instanceof SuperTile)){
            throw new IllegalArgumentException("Expecting a SuperTile checker");
        }
        return negated ^ ((SuperTile)checker).containedTiles(sandbox).contains(((PointLike)target).location(sandbox));
    }
}
