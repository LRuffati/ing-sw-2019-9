package actions;

import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import actions.effects.EffectTemplate;
import actions.utils.ChoiceMaker;
import actions.targeters.Targeter;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import genericitems.Tuple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This represents the action as it is being executed.
 *
 * It will have to contain references to the Sandbox, to the Entity performing choices for the
 * targeter, to the targeters themselves, the previously acquired targets and a method which
 * returns an ActionResult
 */
public class Action {

    private final Sandbox sandbox;
    private final ActionInfo info;
    private final List<Tuple<String, TargeterTemplate>> targeterTemplates;
    private final Map<String, Targetable> previousTargets;
    private final List<EffectTemplate> unresolvedEffects;

    private final Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> finalizer;

    Action(Sandbox sandbox,
           ActionTemplate actionTemplate,
           Map<String, Targetable> previousTargets,
           Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> finalizer){

        this.sandbox = sandbox;
        this.info = actionTemplate.getInfo();
        this.targeterTemplates = actionTemplate.getTargeters();
        this.unresolvedEffects = actionTemplate.getEffects();
        this.previousTargets = previousTargets;
        this.finalizer = finalizer;
    }

    private Action(Sandbox sandbox,
           ActionInfo info,
           List<Tuple<String, TargeterTemplate>> targeters,
           List<EffectTemplate> effects,
           Map<String, Targetable> previousTargets,
           Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> finalizer){
        this.sandbox = sandbox;
        this.info = info;
        this.targeterTemplates = targeters;
        this.unresolvedEffects = effects;
        this.previousTargets = previousTargets;
        this.finalizer = finalizer;
    }

    private static Action spawn(Action old, Map<String, Targetable> targetsUpdated,
                        List<Tuple<String, TargeterTemplate>> remainingTargets){
        return new Action(old.sandbox, old.info, remainingTargets, old.unresolvedEffects,
                targetsUpdated, old.finalizer);
    }

    private static Action spawn(Action old, Sandbox newSandbox,
                              List<EffectTemplate> remainingEffects){
        return new Action(newSandbox, old.info, old.targeterTemplates, remainingEffects,
                old.previousTargets, old.finalizer);
    }

    private ChoiceMaker giveChoiceMaker(boolean automatic, boolean optionalTarg,
                                        Function<Targetable,
            ControllerActionResult> fun) {

        Function<
                Function<Integer, Targetable>, // Bind the variable provided by Targeter
                Function<
                        List<Tuple<Integer, Targetable>>, // Bind target list
                        Function<Integer, ControllerActionResult>
                        >
                > functionAutomatic =
                action ->
                        listTargets ->
                            integer -> {
                                if (listTargets.isEmpty() && optionalTarg) {
                                    return fun.apply(action.apply(-1));
                                } else if (listTargets.isEmpty()) {
                                    return new ControllerActionResult(ActionResultType.ROLLBACK);
                                } else {
                                    return fun.apply(action.apply(listTargets.get(0).x));
                                }
                            };

        Function<
                Function<Integer, Targetable>, // Bind the variable provided by Targeter
                Function<
                        List<Tuple<Integer, Targetable>>, // Bind target list
                        Function<Integer, ControllerActionResult>
                        >
                > functionManual =

                action ->
                        listTargets ->
                                choice -> {
                                    if (choice>=listTargets.size()) choice = 0;

                                    Targetable target = action.apply(listTargets.get(choice).x);
                                    return fun.apply(target);
                                };

        Function<Function<Integer, Targetable>, Function<List<Tuple<Integer, Targetable>>,
                Function<Integer, ControllerActionResult>>> pickFunction;

        if (automatic) pickFunction = functionAutomatic;
        else pickFunction = functionManual;

        return new ChoiceMaker() {

            Function<Integer, Targetable> action;
            List<Tuple<Integer, Targetable>> listTargets = new ArrayList<>();
            boolean optional = optionalTarg;


            @Override
            public void giveTargets(String targetId, List<Targetable> possibilities, Function<Integer, Targetable> action) {
                this.action = action;
                listTargets = IntStream.range(0, possibilities.size())
                        .mapToObj(i -> new Tuple<>(i, possibilities.get(i)))
                        .collect(Collectors.toList());

            }

            @Override
            public Tuple<Boolean, List<Targetable>> showOptions() {
                List<Targetable> listTargetsToShow =
                        listTargets.stream().map(i->i.y).collect(Collectors.toList());
                return new Tuple<>(optional, listTargetsToShow);
            }

            @Override
            public ControllerActionResult pick(int choice) {
                return pickFunction.apply(action).apply(listTargets).apply(choice);
            }
        };
    }

    /*
    Se ha ancora targeter: crea choicemaker, passa la lambda, restituisce il risultato di
    choicemaker

    Se ha ancora effectTemplate: crea la lambda, return il risultato di effect.apply(..., lambda)

    Se non ho più niente: return la finalLambda.apply(sandbox)
     */
    ControllerActionResult iterate(){
        if (!targeterTemplates.isEmpty()){

            Iterator<Tuple<String, TargeterTemplate>> targetersIter= targeterTemplates.iterator();

            //Questa chiamata mi dice anche che targetersIter ora non ha più il primo elemento
            Tuple<String, TargeterTemplate> thisTargeter = targetersIter.next();

            // This is the same for both automatic and manual ChoiceMaker
            Function<Targetable, ControllerActionResult> fun = target -> {
                // This will create a new Action, same sandbox, same effects, a new target,
                // less targeters
                Map<String, Targetable> targetsUpdated = new HashMap<>(previousTargets);
                if (target!=null) targetsUpdated.put(thisTargeter.x, target);

                List<Tuple<String, TargeterTemplate>> remainingTargets = new LinkedList<>();
                targetersIter.forEachRemaining(remainingTargets::add);

                Action newAction = Action.spawn(this,targetsUpdated, remainingTargets);

                return newAction.iterate();
            };

            // Choicemaker creation
            ChoiceMaker choiceMaker =
                    giveChoiceMaker(
                            thisTargeter.y.automatic,
                            thisTargeter.y.optional,
                            fun);

            //Targeter creator
            Targeter targeter =
                    new Targeter(
                            sandbox,
                            choiceMaker,
                            previousTargets,
                            thisTargeter.y,
                            thisTargeter.x);

            if (!targeter.giveChoices()) {
                return new ControllerActionResult(ActionResultType.ROLLBACK);
            }

            // available target or no target
            if (thisTargeter.y.automatic)
                return choiceMaker.pick(0); // In automatic targeters 0 picks the first valid
            else
                return new ControllerActionResult(choiceMaker);
        } else if (!unresolvedEffects.isEmpty()){

            Iterator<EffectTemplate> unresolvedIter= unresolvedEffects.iterator();

            // Retrieve first element and remove it from unresolvediter
            EffectTemplate nextEffect = unresolvedIter.next();

            Function<Sandbox, ControllerActionResult> fun = sandbox1 -> {
                List<EffectTemplate> unresolvedList = new ArrayList<>();
                unresolvedIter.forEachRemaining(unresolvedList::add); // Add all effects except
                // the first one to the list
                Action newAction = Action.spawn(this, sandbox1, unresolvedList);
                return newAction.iterate();
            };

            return nextEffect.spawn(previousTargets, sandbox, fun);
        }
        else return finalizer.apply(new Tuple<>(sandbox, previousTargets));
    }
}
