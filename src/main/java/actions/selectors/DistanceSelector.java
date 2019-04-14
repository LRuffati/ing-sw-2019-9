package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import org.jetbrains.annotations.NotNull;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (distant () pointlike
 */
public class DistanceSelector implements  Selector{
    /**
     * The minimum included distance
     */
    private final int min;

    /**
     * The maximum included distance
     */
    private final int max;

    /**
     * If true don't go through walls
     */
    private final boolean logical;

    /**
     *
     * @param min The minimum included distance
     * @param max The maximum included distance
     * @param logical If true don't go through walls
     */
    public DistanceSelector(int min, int max, boolean logical){
        this.min = min;
        this.max = max;
        this.logical = logical;
    }

    /**
     *
     * @param sourceTarget the point from which to calculate distance
     * @param function the transformation function
     * @return targets within this distance
     */
    public Collection<Targetable> select(@NotNull PointLike sourceTarget, Function<TileUID, Stream<Targetable>> function) {
        return sourceTarget.distanceSelector(min,max,logical).stream().flatMap(function).collect(Collectors.toSet());
    }
}
