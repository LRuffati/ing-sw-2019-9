package actions.selectors;

import actions.targeters.interfaces.PointLike;
import actions.targeters.targets.Targetable;

import java.util.Collection;
import java.util.Map;

public class VisibleSelector implements Selector {
    Collection<Targetable> select(PointLike sourceTarget) {
        return null;
    }

    public Collection<Targetable> select(Map<String, Targetable> validatedTargets) {
        return null;
    }

}
