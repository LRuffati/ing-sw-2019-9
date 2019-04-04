package actions.targeters.interfaces;
import java.util.Optional;

/**
 * This interface will be used when I have to filter some pre-existing targets based on their visibility from a given point
 * In the description language for the effect it is used in the following manner:
 * Visible (... & seen Pointlike)
 */
public interface Visible {
    /**
     * This function filters the targets based on visibility
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return if the object can be seen or partially seen (or not seen) returns the visible (or not visible) sub-selection, otherwise returns empty optional
     */
    Optional<Visible> seen(PointLike source, boolean negation);
}