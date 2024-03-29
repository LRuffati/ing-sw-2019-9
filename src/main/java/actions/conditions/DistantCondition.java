package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import org.jetbrains.annotations.Contract;

import java.util.stream.Collectors;

/**
 * Tests for a distance from the given source
 */
public class DistantCondition implements Condition {
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
     * @param min the minimum (included) number of steps
     * @param max the maximum (included) number of steps
     * @param logical true if I don't want to go through walls
     * @param negated true if I'm interested in targets further or closer than the range
     */
    @Contract(pure = true)
    public DistantCondition(int min, int max, boolean logical, boolean negated){

        this.min = min;
        this.max = max;
        this.logical = logical;
        this.negated = negated;
    }

    /**
     * @param checker has to be a PointLike target
     */
    @Override
    public boolean checkTarget(Sandbox sandbox, Targetable target, Targetable checker) {
        if (checker instanceof PointLike) {
            return negated ^ ((PointLike) checker).distanceSelector(sandbox,min,max,logical)
                    .parallelStream().map(i -> target.getSelectedTiles(sandbox).contains(i))
                    .collect(Collectors.toSet()).contains(Boolean.TRUE);
        }
        else {
            throw new IllegalArgumentException("Checker should be PointLike");
        }
    }
}
