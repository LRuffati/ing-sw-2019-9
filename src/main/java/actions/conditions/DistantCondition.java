package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;

import java.util.stream.Collectors;

/**
 * Checked for the condition target (distant () pointlike)
 */
public class DistantCondition extends Condition {
    private final int min;
    private final int max;
    private final boolean logical;
    private final boolean negated;

    /**
     * The creator class will have a list
     * @param min
     * @param max
     * @param logical
     */
    DistantCondition(int min, int max, boolean logical, boolean negated){

        this.min = min;
        this.max = max;
        this.logical = logical;
        this.negated = negated;
    }

    boolean checkTarget(Targetable target, PointLike checker) {
        return negated ^ checker.distanceSelector(min,max,logical)
                .parallelStream().map(i -> target.getSelectedTiles().contains(i))
                .collect(Collectors.toSet()).contains(Boolean.TRUE);
    }
}
