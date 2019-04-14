package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import org.jetbrains.annotations.NotNull;
import uid.TileUID;

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
    public Collection<Targetable> select(@NotNull PointLike target, @NotNull Function<TileUID,
            Stream<Targetable>> function) {
        return function.apply(target.location()).collect(Collectors.toSet());
    }

}