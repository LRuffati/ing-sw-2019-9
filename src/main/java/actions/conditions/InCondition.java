package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;

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
    @Override
    public boolean checkTarget(Targetable target, Targetable checker) {
        if (!(target instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike target");
        }
        if (!(checker instanceof SuperTile)){
            throw new IllegalArgumentException("Expecting a SuperTile checker");
        }
        return negated ^ ((SuperTile)checker).containedTiles().contains(((PointLike)target).location());
    }
}
