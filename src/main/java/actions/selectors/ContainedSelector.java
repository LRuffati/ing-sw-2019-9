package actions.selectors;

import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the (in Supertile) selector
 */
public class ContainedSelector implements Selector {


    public ContainedSelector(){
        // All information is passed through method calls
    }

    /**
     *
     * @param container the supertile
     * @param function the transform function
     * @return the targets contained in the supertile
     */
    @Override
    public Collection<Targetable> select(Sandbox sandbox, Targetable container,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (container instanceof SuperTile){
            var castedContainer = (SuperTile) container;
            return castedContainer.containedTiles(sandbox).stream()
                    .flatMap(function).collect(Collectors.toSet());
        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }

}
