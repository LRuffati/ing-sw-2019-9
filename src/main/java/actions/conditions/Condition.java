package actions.conditions;

import actions.targeters.targets.Targetable;
import board.Sandbox;

public abstract class Condition {
    /**
     *
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    //TODO: check that the overloading works as intended, test using a List<Condition> containing
    // conditions of different types, such as DistantCondition and SeenCondition
    public abstract boolean checkTarget(Sandbox sandbox, Targetable target, Targetable checker);

}