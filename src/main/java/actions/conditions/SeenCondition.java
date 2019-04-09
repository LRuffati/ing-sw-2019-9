package actions.conditions;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.Visible;

public class SeenCondition extends Condition {
    private final boolean negate;

    SeenCondition(boolean negate){

        this.negate = negate;
    }

    boolean checkTarget(Visible target,  PointLike checker) {
        return target.seen(checker, negate);
    }
}
