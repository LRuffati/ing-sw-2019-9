package actions.conditions;

import actions.targeters.targets.Targetable;
import board.Sandbox;

/**
 * Interface that checks if a Target validate certain conditions
 */
public interface Condition {
    /**
     * @param sandbox the sandbox in which to test the condition
     * @param target the target being tested
     * @param checker the source of the check
     * @return true if target is valid
     */
    //TODO: check that the overloading works as intended, test using a List<Condition> containing
    // conditions of different types, such as DistantCondition and SeenCondition
    public abstract boolean checkTarget(Sandbox sandbox, Targetable target, Targetable checker);

}