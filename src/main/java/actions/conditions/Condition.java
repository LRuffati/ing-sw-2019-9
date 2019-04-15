package actions.conditions;

import actions.targeters.targets.Targetable;

public abstract class Condition {
    private boolean negate;

    /**
     *
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    //TODO: check that the overloading works as intended, test using a List<Condition> containing
    // conditions of different types, such as DistantCondition and SeenCondition
    public abstract boolean checkTarget(Targetable target, Targetable checker);

}