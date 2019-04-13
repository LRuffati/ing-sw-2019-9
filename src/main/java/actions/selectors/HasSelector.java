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


    HasSelector(){}

    Collection<Targetable> select(PointLike target, Function<TileUID, Stream<Targetable>> function) {
        return function.apply(target.location()).collect(Collectors.toSet());
    }

}