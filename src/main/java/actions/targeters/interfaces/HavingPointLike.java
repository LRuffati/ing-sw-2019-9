package actions.targeters.interfaces;

import java.util.Optional;

/**
 * This interface models the condition:
 *  + targetName HavingPointLike (... & has PointLike)
 * as well as the selector:
 *  + targetName HavingPointLike (has PointLike)
 */
public interface HavingPointLike {
    /**
     *
     * @param target
     * @return an empty optional if the
     */
    Optional<HavingPointLike> filteringHas(PointLike target);
    HavingPointLike selectingHas(PointLike target);
}
