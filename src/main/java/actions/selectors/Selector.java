package actions.selectors;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The selector is a positive condition which generates a list of all the elements which satisfy it
 */
public interface Selector {

    /**
     *
     * @param sourceTarget the target provided as a source of the selector condition
     * @param converter the function converting TileUID into the appropriate target
     * @return a collection of the required Targets
     */
    //TODO: test calling different Selectors from a list, create a list of selectors of different
    // kinds intermixed and check that it returns the appropriate results
    Collection<Targetable> select(Sandbox sandbox, Targetable sourceTarget,
                                  Function<TileUID, Stream<Targetable>> converter);
}