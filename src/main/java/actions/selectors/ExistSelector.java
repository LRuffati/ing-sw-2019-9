package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExistSelector implements Selector{
    private final Sandbox sandbox;

    ExistSelector(Sandbox sandbox){
        this.sandbox = sandbox;
    }

    @Override
    public Collection<Targetable> select(Targetable sourceTarget, Function<TileUID, Stream<Targetable>> function) {
        return sandbox.allTiles().stream()
                .flatMap(function).collect(Collectors.toSet());
    }
}
