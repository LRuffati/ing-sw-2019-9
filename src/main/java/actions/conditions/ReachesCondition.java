package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;

import java.util.stream.Collectors;


/**
 * target (... & reaches selector)
 */
public class ReachesCondition extends Condition {
    private final int min;
    private final int max;
    private final boolean negate;

    ReachesCondition(int min, int max, boolean negate){
        this.min = min;
        this.max = max;
        this.negate = negate;
    }

    boolean checkTarget(Targetable target,  PointLike checker) {
        return negate ^ checker.reachableSelector(min,max)
                .parallelStream().map((i)-> target.getSelectedTiles().contains(i))
                .collect(Collectors.toSet()).contains(Boolean.TRUE);
    }
}
