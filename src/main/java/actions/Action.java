package actions;

import actions.effects.ActionResult;
import actions.effects.EffectTemplate;
import actions.effects.EffectType;
import actions.targeters.ChoiceMaker;
import actions.targeters.Targeter;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import actions.utils.NotEnoughTargetsException;
import board.Sandbox;
import genericitems.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This represents the action as it is being executed.
 *
 * It will have to contain references to the Sandbox, to the Entity performing choices for the
 * targeter, to the targeters themselves, the previously acquired targets and a method which
 * returns an ActionResult
 */
public class Action {

    private final Sandbox sandbox;
    private final Optional<String> master;
    private final String actionID;
    private final List<Tuple<String, TargeterTemplate>> targeterTemplates;
    private final Map<EffectType, Collection<EffectTemplate>> effects;
    private final Map<String, Targetable> previousTargets;

    Action(Sandbox sandbox, Optional<String> master, String actionID,
           List<Tuple<String, TargeterTemplate>> targeters,
           Map<EffectType, Collection<EffectTemplate>> effects,
           Map<String, Targetable> previousTargets){

        this.sandbox = sandbox;
        this.master = master;
        this.actionID = actionID;
        this.targeterTemplates = targeters;
        this.effects = effects;
        this.previousTargets = previousTargets;
    }

    /**
     * This is the interactive version of the method, it will perform calls to the
     * provided ChoiceMaker to select the various targets
     * @param choiceMaker the ChoiceMaker which will perform the choices
     * @return an ActionResult
     */
    public ActionResult runAction(ChoiceMaker choiceMaker) throws NotEnoughTargetsException {
        for (Tuple<String, TargeterTemplate> i:targeterTemplates){
            Targeter targeter = new Targeter(sandbox, choiceMaker, previousTargets, i.y, i.x);
            Optional<Targetable> target = targeter.generateTarget();
            if (target.isPresent()){
                previousTargets.put(i.x, target.get());
            }
        }
        return new ActionResult(effects, previousTargets);
    }

    /**
     * This version is a verifier, it creates a custom version of ChoiceMaker
     * @param targets targets already acquired by the previous actions
     * @return
     */
    public ActionResult runAction(Map<String, Targetable> targets) throws NotEnoughTargetsException {
        ChoiceMaker choiceMaker = new ChoiceMaker (){

            /**
             * Choose a target amongst many possibilities
             *
             * @param targetId the id of the target being selected
             * @param possibilities an ordered list of which Targets are available. The elements should be copies of the ones on the server and not the actual ones to avoid contamination
             * @return the number of the chosen target, starting from 0
             */
            @Override
            public int pickTarget(String targetId, List<Targetable> possibilities) {
                if (!possibilities.contains(targets.get(targetId))){
                    throw new IllegalArgumentException("Target not available");
                }
                return possibilities.indexOf(targets.get(targetId));
            }
        };
        return runAction(choiceMaker);
    }


}
