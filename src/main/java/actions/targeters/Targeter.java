package actions.targeters;


import actions.conditions.Condition;
import actions.targeters.interfaces.TargetedSelector;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.nio.channels.Selector;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * This class, one per action, generates the mapping from string to Target used by the effects
 * If interactivity is needed it will call on a class given during instantiation
 */
public class Targeter {
    /**
     *
     * @param sandbox the sandbox in which the weapon usage is being simulated
     * @param selector the selector used to generate a first list of targets
     * @param filters the filters used to trim down the available targets
     * @param master the class, implementing interface ChoiceMaker, which will select the target amongst many options
     * @param previousTargets the previously selected target
     */
    Targeter(Sandbox sandbox, Selector selector, Collection<Condition> filters, ChoiceMaker master, Map<String, Targetable> previousTargets, Function<Collection<Targetable>, Collection<Targetable>> fromSelected){

    }



}
