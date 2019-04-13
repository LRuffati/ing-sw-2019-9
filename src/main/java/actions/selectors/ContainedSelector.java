package actions.selectors;

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
public class ContainedSelector implements Selector {


    ContainedSelector(){
    }

    Collection<Targetable> select(SuperTile container, Function<TileUID, Stream<Targetable>> function) {
        return container.containedTiles().stream().flatMap(function)
                .collect(Collectors.toSet());
    }

}
