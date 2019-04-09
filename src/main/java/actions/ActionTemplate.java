package actions;

import actions.targeters.Targeter;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.List;
import java.util.Map;

public class ActionTemplate {

    private List<String> targetsToAcquire;
    private Map<String, Targeter> targeters;

    /**
     * @param gameStatus
     * @return
     */
    Action spawn(Sandbox gameStatus, Map<String, Targetable> existingTargets){
        return null;
    }
}
