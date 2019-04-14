package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.Visible;

/**
 * target (... &amp; seen checker)
 */
public class SeenCondition extends Condition {
    /**
     * Whether I'm looking at a positive or negative condition
     */
    private final boolean negate;

    /**
     *
     * @param negate true if I'm interested in unseen targets
     */
    public SeenCondition(boolean negate){
        this.negate = negate;
    }

    /**
     *
     * @param target the target being filtered
     * @param checker the filter
     * @return the result of the check
     */
    public boolean checkTarget(Visible target,  PointLike checker) {
        return target.seen(checker, negate);
    }
}
