package actions.targeters;

import actions.targeters.targets.Targetable;

import java.util.List;

public interface ChoiceMaker {
    /**
     * Choose a target amongst many possibilities
     * @param targetId the id of the target being selected
     * @param possibilities an ordered list of which Targets are available. The elements should be copies of the ones on the server and not the actual ones to avoid contamination
     * @return the number of the chosen target, starting from 0
     */
    int pickTarget(String targetId, List<Targetable> possibilities);
}
