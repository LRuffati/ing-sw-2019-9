package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;

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
    @Override
    public boolean checkTarget(Targetable target, Targetable checker) {
        if (!(target instanceof SuperTile)){
            throw new IllegalArgumentException("Expecting a Supertile target");
        }
        if (!(checker instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike checker");
        }
        return negated ^ ((SuperTile) target).containedTiles().contains(((PointLike) checker).location());
    }
}
