package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.stream.Collectors;


/**
 * checks that the target can reach the source in the given amount of moves
 */
public class ReachesCondition implements Condition {
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
     * @param target should be PointLike
     */
    @Override
    public boolean checkTarget(Sandbox sandbox, Targetable target,  Targetable checker) {
        if (!(target instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike checker");
        }
        return negate ^ ((PointLike) target).reachableSelector(sandbox, min,max).stream()
                .anyMatch(tileUID -> checker.getSelectedTiles(sandbox).contains(tileUID));
                /*
                1. get all tiles reachable by the target in the given moves
                2. check that source is in it
                 */
    }
}
