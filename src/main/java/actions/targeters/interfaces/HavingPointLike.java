package actions.targeters.interfaces;

/**
 * This interface models the condition:
 *  + targetName HavingPointLike (... & has PointLike)
 * as well as the selector:
 *  + targetName HavingPointLike (has PointLike)
 */
public interface HavingPointLike {
    /**
     * @param target
     * @param negation
     * @return an empty optional if the
     */
    boolean filteringHas(PointLike target, boolean negation);
}
