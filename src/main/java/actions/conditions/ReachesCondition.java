package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.stream.Collectors;


/**
 * target (... &amp; reaches selector)
 */
public class ReachesCondition extends Condition {
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
    private final boolean negate;

    /**
     *
     * @param min the minimum (included) number of steps
     * @param max the maximum (included) number of steps
     * @param negate true if I'm looking for unreachable targets
     */
    public ReachesCondition(int min, int max, boolean negate){
        this.min = min;
        this.max = max;
        this.negate = negate;
    }
    /**
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    @Override
    public boolean checkTarget(Sandbox sandbox, Targetable target,  Targetable checker) {
        if (!(checker instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike checker");
        }
        return negate ^ ((PointLike) checker).reachableSelector(sandbox, min,max)
                .parallelStream().map(i -> target.getSelectedTiles(sandbox).contains(i))
                .collect(Collectors.toSet()).contains(Boolean.TRUE);
    }
}
