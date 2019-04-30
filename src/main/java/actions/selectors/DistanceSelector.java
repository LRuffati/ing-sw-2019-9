package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import org.jetbrains.annotations.NotNull;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (distant () pointlike
 */
public class DistanceSelector implements  Selector{
    /**
     * The minimum included distance
     */
    private final int min;

    /**
     * The maximum included distance
     */
    private final int max;

    /**
     * If true don't go through walls
     */
    private final boolean logical;

    /**
     *
     * @param min The minimum included distance
     * @param max The maximum included distance
     * @param logical If true don't go through walls
     */
    public DistanceSelector(int min, int max, boolean logical){
        this.min = min;
        this.max = max;
        this.logical = logical;
    }

    /**
     *
     * @param sourceTarget the point from which to calculate distance
     * @param function the transformation function
     * @return targets within this distance
     */
    @Override
    public Collection<Targetable> select(Sandbox sandbox, Targetable sourceTarget,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (sourceTarget instanceof PointLike){
            var castedContainer = (PointLike) sourceTarget;
            return castedContainer.distanceSelector(sandbox,min,max,logical).stream()
                    .flatMap(function).collect(Collectors.toSet());
        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }
}
