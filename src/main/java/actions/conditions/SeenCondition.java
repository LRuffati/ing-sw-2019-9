package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.Visible;
import actions.targeters.targets.Targetable;
import board.Sandbox;

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
    @Override
    public boolean checkTarget(Sandbox sandbox, Targetable target, Targetable checker) {
        if (!(target instanceof Visible)){
            throw new IllegalArgumentException("Expecting a Visible target");
        }
        if (!(checker instanceof PointLike)){
            throw new IllegalArgumentException("Expecting a PointLike checker");
        }

        return ((Visible) target).seen(sandbox, (PointLike) checker, negate);
    }
}
