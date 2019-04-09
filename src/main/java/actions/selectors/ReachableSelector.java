package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReachableSelector implements Selector {
    private final int min;
    private final int max;
    private final boolean logical;
    private final Function<TileUID, Stream<Targetable>> function;

    ReachableSelector(int min, int max, boolean logical, Function<TileUID, Stream<Targetable>> function){

        this.min = min;
        this.max = max;
        this.logical = logical;
        this.function = function;
    }

    Collection<Targetable> select(PointLike source){
        return source.distanceSelector(min, max, logical).stream().flatMap(function)
                .collect(Collectors.toSet());
    }
}
