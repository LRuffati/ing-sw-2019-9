package actions.targeters;

import actions.conditions.Condition;
import actions.selectors.Selector;
import genericitems.Tuple;

import java.util.List;

/**
 * This class holds the information about a targeter without needing a sandbox, nor providing any methods
 *
 * The class should be immutable to allow easier passing without worrying about performing a deep
 * copy
 */
public class TargeterTemplate {
    /**
     * The String is the name of the Targetable element which the Selector needs, the Selector
     * exposes a `select` function which will be the first to be invoked by the targeter
     *
     * The String represents a key in the previous targets Map in the actual targeter
     */
    final Tuple<String, Selector> selector;

    /**
     * @see TargeterTemplate#selector
     */
    final List<Tuple<String, Condition>> filters;

    /**
     * The string representation of the Target type
     * @see Targeter
     */
    final String type;

    /**
     * Some targets may not be acquired, for instance in cases where a weapon targets multiple
     * elements but only a limited number is available
     */
    final boolean optional;

    /**
     * Some actions require a target to be different from any other target effected by the WeaponUse
     */
    final boolean newTarg;

    /**
     * Some targets may be acquired automatically without the need for user input, for instance
     * the Pawns occupying a tile where the user selects the tile and all characters inside are
     * affected
     */
    final boolean automatic;

    TargeterTemplate(Tuple<String, Selector> selector,
                     List<Tuple<String, Condition>> filters,
                     String type,
                     boolean optional, boolean newTarg, boolean automatic) {

        this.selector = selector;
        this.filters = filters;
        this.type = type;
        this.optional = optional;
        this.newTarg = newTarg;
        this.automatic = automatic;
    }

}
