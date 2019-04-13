package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReachableSelector implements Selector {
    private final int min;
    private final int max;
    private final boolean logical;

    ReachableSelector(int min, int max, boolean logical){

        this.min = min;
        this.max = max;
        this.logical = logical;
    }

    Collection<Targetable> select(PointLike source, Function<TileUID, Stream<Targetable>> function){
        return source.distanceSelector(min, max, logical).stream().flatMap(function)
                .collect(Collectors.toSet());
    }
}
