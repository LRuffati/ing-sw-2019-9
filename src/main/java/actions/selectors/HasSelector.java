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
 * This class represents the (in Supertile) selector
 */
public class HasSelector implements Selector {

    public HasSelector(){
        // All information is provided by method arguments
    }

    /**
     *
     * @param target the pointlike target
     * @param function the transformation function
     * @return a list with a single element
     */
    @Override
    public Collection<Targetable> select(Sandbox sandbox, Targetable target,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (target instanceof PointLike){
            var castedContainer = (PointLike) target;
            return function.apply(castedContainer.location(sandbox)).collect(Collectors.toSet());

        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }

}