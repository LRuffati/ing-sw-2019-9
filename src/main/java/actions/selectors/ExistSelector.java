package actions.selectors;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.Targetable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import uid.TileUID;

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
    ExistSelector(){}

    /**
     *
     * @param self the target representing the invoker of the action
     * @param function the transformation function
     * @return all targets in the sandbox
     */
    public Collection<Targetable> select(@NotNull BasicTarget self,
                                         Function<TileUID, Stream<Targetable>> function) {
        return self.coexistingTiles().stream()
                .flatMap(function).collect(Collectors.toSet());
    }
}
