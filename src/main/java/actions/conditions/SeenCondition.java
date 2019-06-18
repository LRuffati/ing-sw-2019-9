package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.Visible;
import actions.targeters.targets.Targetable;
import board.Sandbox;

/**
 * Checks whether a target is seen by the checker or not
 */
public class SeenCondition implements Condition {
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
     * @param target should be a Visible
     * @param checker should be a PointLike
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
