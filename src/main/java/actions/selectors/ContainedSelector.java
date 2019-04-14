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

    /**
     *
     * @param container the supertile
     * @param function the transform function
     * @return the targets contained in the supertile
     */
    Collection<Targetable> select(SuperTile container, Function<TileUID, Stream<Targetable>> function) {
        return container.containedTiles().stream().flatMap(function)
                .collect(Collectors.toSet());
    }

}
