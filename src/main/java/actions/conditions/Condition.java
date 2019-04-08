package actions.conditions;

import actions.targeters.targets.Targetable;

public abstract class Condition {
    private boolean negate;

    abstract boolean checkTarget(Targetable target);

}