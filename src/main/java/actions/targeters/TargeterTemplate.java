package actions.targeters;

import actions.conditions.Condition;
import actions.selectors.Selector;
import genericitems.Tuple;

import java.util.List;

public class TargeterTemplate {
    final Tuple<String, Selector> selector;
    final List<Tuple<String, Condition>> filters;
    final String type;
    final boolean optional;
    final boolean newTarg;
    final boolean automatic;

    TargeterTemplate(Tuple<String, Selector> selector,
                     List<Tuple<String, Condition>> filters,
                     String type,
                     boolean optional, boolean newTarg, boolean automatic){

        this.selector = selector;
        this.filters = filters;
        this.type = type;
        this.optional = optional;
        this.newTarg = newTarg;
        this.automatic = automatic;
    }
}
