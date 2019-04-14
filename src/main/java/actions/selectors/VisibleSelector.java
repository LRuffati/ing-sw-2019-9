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
 * This class implements the selector:
 * (seen Pointlike)
 */
public class VisibleSelector implements Selector {

    VisibleSelector(){}

    /**
     *
     * @param source a pointlike target
     * @param function the conversion function from [TileUID] to [Targetable]
     * @return all the targets seen by the source
     */
    Collection<Targetable> select(@NotNull PointLike source, Function<TileUID, Stream<Targetable>> function){
        return source.tilesSeen().stream().flatMap(function).collect(Collectors.toSet());
    }
}
