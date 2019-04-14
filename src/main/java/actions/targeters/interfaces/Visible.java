package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;

/**
 * This interface will be used when I have to filter some pre-existing targets based on their visibility from a given point
 * In the description language for the effect it is used in the following manner:
 * Visible (... &amp; seen Pointlike)
 */
public interface Visible extends Targetable {
    /**
     * This function filters the targets based on visibility
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR whether the source can se the target
     */
    boolean seen(PointLike source, boolean negation);
}
