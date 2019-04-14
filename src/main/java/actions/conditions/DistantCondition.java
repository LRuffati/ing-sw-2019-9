package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;

import java.util.stream.Collectors;

/**
 * Checked for the condition target (distant () pointlike)
 */
public class DistantCondition extends Condition {
    /**
     * the minimum (included) number of steps
     */
    private final int min;

    /**
     * the maximum (included) number of steps
     */
    private final int max;

    /**
     * true if I'm looking for unreachable targets
     */
    private final boolean negated;

    /**
     * If true don't go through walls
     */
    private final boolean logical;


    /**
     *
     * @param min the minimum (included) number of steps
     * @param max the maximum (included) number of steps
     * @param logical true if I don't want to go through walls
     * @param negated true if I'm interested in targets further or closer than the range
     */
    public DistantCondition(int min, int max, boolean logical, boolean negated){

        this.min = min;
        this.max = max;
        this.logical = logical;
        this.negated = negated;
    }

    /**
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    public boolean checkTarget(Targetable target, PointLike checker) {
        return negated ^ checker.distanceSelector(min,max,logical)
                .parallelStream().map(i -> target.getSelectedTiles().contains(i))
                .collect(Collectors.toSet()).contains(Boolean.TRUE);
    }
}
