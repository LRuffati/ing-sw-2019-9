package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the (in Supertile) selector
 */
public class HasSelector implements Selector {

    private final Function<TileUID, Stream<Targetable>> function;

    HasSelector(Function<TileUID, Stream<Targetable>> function){
        this.function = function;
    }

    Collection<Targetable> select(PointLike target) {
        return function.apply(target.location()).collect(Collectors.toSet());
    }

}