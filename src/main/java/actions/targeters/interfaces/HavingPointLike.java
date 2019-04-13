package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;

/**
 * This interface models the condition:
 *  + targetName HavingPointLike (... & has PointLike)
 * as well as the selector:
 *  + targetName HavingPointLike (has PointLike)
 */
public interface HavingPointLike extends Targetable {
    /**
     * @param target
     * @param negation
     * @return an empty optional if the
     */
    boolean filteringHas(PointLike target, boolean negation);
}
