package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (distant () pointlike
 */
public class DistanceSelector implements  Selector{
    private final Function<TileUID, Stream<Targetable>> function;
    private final int min;
    private final int max;
    private final boolean logical;

    DistanceSelector(Function<TileUID, Stream<Targetable>> function, int min, int max, boolean logical){

        this.function = function;
        this.min = min;
        this.max = max;
        this.logical = logical;
    }

    Collection<Targetable> select(PointLike sourceTarget) {
        return sourceTarget.distanceSelector(min,max,logical).stream().flatMap(function).collect(Collectors.toSet());
    }
}
