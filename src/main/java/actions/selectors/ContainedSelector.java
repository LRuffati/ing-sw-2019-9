package actions.selectors;

import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;

import java.util.Collection;
import java.util.Map;

public class ContainedSelector implements Selector {
    Collection<Targetable> select(SuperTile container) {
        return null;
    }

    public Collection<Targetable> select(Map<String, Targetable> validatedTargets) {
        return null;
    }

}
