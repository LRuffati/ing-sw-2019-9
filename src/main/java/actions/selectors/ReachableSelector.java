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
 * This condition returns all targets reachable by the source in the given steps
 */
public class ReachableSelector implements Selector {
    /**
     * the minimum (included) number of steps
     */
    private final int min;

    /**
     * the maximum (included) number of steps
     */
    private final int max;

    /**
     *
     * @param min the minimum (included) number of steps
     * @param max the maximum (included) number of steps
     */
    public ReachableSelector(int min, int max){

        this.min = min;
        this.max = max;
    }

    /**
     *
     * @param source a pointlike target
     * @param function the transformation function
     * @return all the targets reachable by the source in the given amount of steps
     */
    //TODO: test with a DominationPoint as source
    @Override
    public Collection<Targetable> select(Targetable source,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (source instanceof PointLike){
            var castedContainer = (PointLike) source;
            return castedContainer.reachableSelector(min, max).stream()
                    .flatMap(function)
                    .collect(Collectors.toSet());

        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }
}
