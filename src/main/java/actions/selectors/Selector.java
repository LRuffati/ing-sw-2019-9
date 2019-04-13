package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Selector {
    default Collection<Targetable> select(Targetable sourceTarget, Function<TileUID, Stream<Targetable>> converter) {
        throw new InvalidParameterException("Targeter not compatible with action");
    }
}