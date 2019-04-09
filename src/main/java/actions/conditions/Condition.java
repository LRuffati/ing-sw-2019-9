package actions.conditions;

import actions.targeters.targets.Targetable;

public abstract class Condition {
    private boolean negate;

    boolean checkTarget(Targetable target){
        throw new IllegalArgumentException("Invalid Target");
    }

}