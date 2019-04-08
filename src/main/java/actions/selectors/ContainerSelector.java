package actions.selectors;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.targets.Targetable;

import java.util.Collection;
import java.util.Map;

public class ContainerSelector implements Selector {

    ContainerSelector(String targeterName){

    }

    Collection<Targetable> select(HavingPointLike sourceTarget) {
        return null;
    }

    public Collection<Targetable> select(Map<String, Targetable> validatedTargets) {
        return null;
    }

}
