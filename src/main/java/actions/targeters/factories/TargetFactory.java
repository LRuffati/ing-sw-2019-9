package actions.targeters.factories;

import actions.targeters.targets.Targetable;

public interface TargetFactory {
    Targetable fromTiles(SelectorOutput targetInfo);
}
