package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VisibleSelector implements Selector {

    VisibleSelector(){}

    Collection<Targetable> select(PointLike source, Function<TileUID, Stream<Targetable>> function){
        return source.tilesSeen().stream()
                .flatMap(function).collect(Collectors.toSet());
    }
}
