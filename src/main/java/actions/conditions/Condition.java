package actions.conditions;

import actions.targeters.targets.Targetable;

public abstract class Condition {
    private boolean negate;

    public boolean checkTarget(Targetable target, Targetable checker){
        throw new IllegalArgumentException("Invalid Target");
    }

}