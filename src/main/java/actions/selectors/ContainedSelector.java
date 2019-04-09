package actions.selectors;

import actions.targeters.interfaces.SuperTile;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.Collection;
import java.util.Map;

/**
 * This class represents the (in Supertile) selector
 */
public class ContainedSelector implements Selector {

    Collection<Targetable> select(SuperTile container, Sandbox sandbox) {
        return null;
    }

    //Todo: add references to Sandbox
    public Collection<Targetable> select(Map<String, Targetable> validatedTargets, Sandbox sandbox) {
        return null;
    }

}
