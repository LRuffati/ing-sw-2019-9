package actions.selectors;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import org.jetbrains.annotations.Contract;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The universal selector, provides all the targets in a given sandbox, in the configuration file:
 * targetType (exists)
 * Then it becomes
 * ("self", new ExistSelector())
 */
public class ExistSelector implements Selector{

    @Contract(pure = true)
    public ExistSelector(){
        // All information is provided by method arguments
    }

    /**
     *
     * @param self the target representing the invoker of the action
     * @param function the transformation function
     * @return all targets in the sandbox
     */
    @Override
    public Collection<Targetable> select(Sandbox sandbox, Targetable self,
                                         Function<TileUID, Stream<Targetable>> function) {
        if (self instanceof BasicTarget){
            var castedContainer = (BasicTarget) self;
            return castedContainer.coexistingTiles(sandbox).stream()
                    .flatMap(function).collect(Collectors.toSet());

        } else {
            throw new InvalidParameterException("Targeter not compatible with action");
        }
    }
}
