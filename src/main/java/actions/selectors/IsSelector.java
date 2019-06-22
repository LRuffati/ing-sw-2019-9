package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Provides the same selector, useful for machine gun only
 */
public class IsSelector implements Selector{
    /**
     * @param sourceTarget the target provided as a source of the selector condition
     * @return a list containing the target provided
     */
    @Override
    public Collection<Targetable> select(Sandbox sandbox, Targetable sourceTarget, Function<TileUID, Stream<Targetable>> converter) {
        return List.of(sourceTarget);
    }
}
