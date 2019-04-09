package actions.conditions;

import actions.targeters.interfaces.SuperTile;
import board.Sandbox;


/**
 * target (... & reaches selector)
 */
public class ReachesCondition extends Condition {
    private final int min;
    private final int max;
    private final boolean negate;
    private final Sandbox sandbox;

    ReachesCondition(int min, int max, boolean negate, Sandbox sandbox){
        this.min = min;
        this.max = max;
        this.negate = negate;
        this.sandbox = sandbox;
    }

    boolean checkTarget(SuperTile target) {
        return false;
    }
}
