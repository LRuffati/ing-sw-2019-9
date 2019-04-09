package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExistSelector implements Selector{
    private final Sandbox sandbox;
    private final Function<TileUID, Stream<Targetable>> function;

    ExistSelector(Sandbox sandbox, Function<TileUID, Stream<Targetable>> function){
        this.sandbox = sandbox;
        this.function = function;
    }

    @Override
    public Collection<Targetable> select(Targetable sourceTarget) {
        return sandbox.allTiles().stream()
                .flatMap(function).collect(Collectors.toSet());
    }
}
