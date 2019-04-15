package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import org.jetbrains.annotations.NotNull;
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

    public HasSelector(){}

    /**
     *
     * @param target the pointlike target
     * @param function the transformation function
     * @return a list with a single element
     */
    @Override
    public Collection<Targetable> select(Targetable target,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (target instanceof PointLike){
            var castedContainer = (PointLike) target;
            return function.apply(castedContainer.location()).collect(Collectors.toSet());

        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }

}