package actions.targeters.interfaces;

import java.util.Collection;

/**
 * This interface models the condition:
 *  + targetName HavingPointLike (... & has PointLike)
 * as well as the selector:
 *  + targetName HavingPointLike (has PointLike)
 */
public interface HavingPointLike {
    /**
     * @param target
     * @return an empty optional if the
     */
    boolean filteringHas(PointLike target);

    /**
     * @param target
     * @return
     */
    Collection<HavingPointLike> selectingHas(PointLike target);
}
