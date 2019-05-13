package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import board.Sandbox;

/**
 * This interface models the condition:
 *  + targetName HavingPointLike (... &amp; has PointLike)
 * as well as the selector:
 *  + targetName HavingPointLike (has PointLike)
 */
public interface HavingPointLike extends Targetable {
    /**
     * @param target the target which I'm checking is in the HavingPointLike object
     * @param negation if I'm looking for a positive or negative check
     * @return negation xor (target is contained in the Object)
     */
    boolean filteringHas(Sandbox sandbox, PointLike target, boolean negation);
}
