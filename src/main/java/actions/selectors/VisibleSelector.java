package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class implements the selector:
 * (seen Pointlike)
 */
public class VisibleSelector implements Selector {

    public VisibleSelector(){
        // All information is provided by method arguments
    }

    /**
     *
     * @param source a pointlike target
     * @param function the conversion function from [TileUID] to [Targetable]
     * @return all the targets seen by the source
     */
    public Collection<Targetable> select(Sandbox sandbox, Targetable source,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (source instanceof PointLike){
            var castedContainer = (PointLike) source;
            return castedContainer.tilesSeen(sandbox).stream()
                    .flatMap(function).collect(Collectors.toSet());

        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }
}
