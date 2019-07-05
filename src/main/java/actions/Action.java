package actions;

import actions.effects.Effect;
import actions.effects.EffectTemplate;
import actions.utils.ActionPicker;
import actions.utils.AmmoAmount;
import actions.utils.ChoiceMaker;
import actions.targeters.Targeter;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import controller.controllermessage.*;
import genericitems.Tuple;
import viewclasses.ActionView;
import viewclasses.TargetView;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Action {

    private final Sandbox sandbox;
    private final ActionInfo info;
    private final List<Tuple<String, TargeterTemplate>> targeterTemplates;
    private final Map<String, Targetable> previousTargets;
    private final List<EffectTemplate> unresolvedEffects;

    private final Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerMessage> finalizer;

    Action(Sandbox sandbox,
           ActionTemplate actionTemplate,
           Map<String, Targetable> previousTargets,
           Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerMessage> finalizer){

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
           Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerMessage> finalizer){
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
                                                ControllerMessage> fun) {

        Function<
                Function<Integer, Targetable>, // Bind the variable provided by Targeter
                Function<
                        List<Tuple<Integer, TargetView>>, // Bind target list
                        Function<Integer, ControllerMessage>
                        >
                > functionAutomatic =
                action -> // Goes from the integer to the target
                        listTargets -> //
                            integer -> {
                                if (listTargets.isEmpty() && optionalTarg) {
                                    return fun.apply(action.apply(-1));
                                } else if (listTargets.isEmpty()) {
                                    return new RollbackMessage("No targets are available from " +
                                            "this position");
                                } else {
                                    return fun.apply(action.apply(listTargets.get(0).x));
                                }
                            };

        Function<
                Function<Integer, Targetable>, // Bind the variable provided by Targeter
                Function<
                        List<Tuple<Integer, TargetView>>, // Bind target list
                        Function<Integer, ControllerMessage>
                        >
                > functionManual =

                action ->
                        listTargets ->
                                choice -> {
                                    if (choice < 0)
                                        return fun.apply(null);

                                    Targetable target = action.apply(listTargets.get(choice).x);
                                    return fun.apply(target);
                                };

        Function<Function<Integer, Targetable>, Function<List<Tuple<Integer, TargetView>>,
                Function<Integer, ControllerMessage>>> pickFunction;

        if (automatic) pickFunction = functionAutomatic;
        else pickFunction = functionManual;

        return new ChoiceMaker() {

            private String description;
            Function<Integer, Targetable> action;
            List<Tuple<Integer, TargetView>> listTargets = new ArrayList<>();
            boolean optional = optionalTarg;


            @Override
            public void giveTargets(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action) {
                this.action = action;
                listTargets = IntStream.range(0, possibilities.size())
                        .mapToObj(i -> new Tuple<>(i, possibilities.get(i)))
                        .collect(Collectors.toList());

                this.description = "";

            }

            @Override
            public void giveTargetsWithDescr(String targetId, List<TargetView> possibilities, Function<Integer, Targetable> action, String description) {
                this.action = action;
                listTargets = IntStream.range(0, possibilities.size())
                        .mapToObj(i -> new Tuple<>(i, possibilities.get(i)))
                        .collect(Collectors.toList());
                this.description = description;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Tuple<Boolean, List<TargetView>> showOptions() {
                List<TargetView> listTargetsToShow =
                        listTargets.stream().map(i->i.y).collect(Collectors.toList());
                return new Tuple<>(optional, listTargetsToShow);
            }

            @Override
            public ControllerMessage pick(int choice) {
                // Deve gestire solo -1 o un numero valido
                return pickFunction.apply(action).apply(listTargets).apply(choice);
            }
        };
    }


    public ControllerMessage iterate(){
        return iterate(false);
    }

    /*
    Se ha ancora targeter: crea choicemaker, passa la lambda, restituisce il risultato di
    choicemaker

    Se ha ancora effectTemplate: crea la lambda, return il risultato di effect.apply(..., lambda)

    Se non ho più niente: return la finalLambda.apply(sandbox)
     */
     public ControllerMessage iterate(boolean contempDone){
         List<ActionTemplate> availableContemp = info.getContempList().stream()
                 .filter(i -> i.actionAvailable(
                         previousTargets,
                         List.of(info.getActionId()),
                         new AmmoAmount(sandbox.getUpdatedTotalAmmoAvailable())
                 )).collect(Collectors.toList());

         if (contempDone)
             availableContemp.clear();

        if (!targeterTemplates.isEmpty()){

            Iterator<Tuple<String, TargeterTemplate>> targetersIter=
                    new ArrayList<>(targeterTemplates).iterator();

            //Questa chiamata mi dice anche che targetersIter ora non ha più il primo elemento
            Tuple<String, TargeterTemplate> thisTargeter = targetersIter.next();

            List<Tuple<String, TargeterTemplate>> remainingTargeters = new ArrayList<>();
            targetersIter.forEachRemaining(remainingTargeters::add);
            // This is the same for both automatic and manual ChoiceMaker
            // Will create a new action with the same sandbox but a new target and a reduced
            // targeter list
            Function<Targetable, ControllerMessage> fun = target -> {
                // Deve gestire null (se opzionale) o un target
                boolean optional = thisTargeter.y.optional;
                Map<String, Targetable> targetsUpdated = new HashMap<>(previousTargets);
                if (target!=null) targetsUpdated.put(thisTargeter.x, target);
                if (target==null && !optional) return new RollbackMessage("Devi selezionare " +
                        "un bersaglio obbligatoriamente");

                Action newAction = Action.spawn(this,targetsUpdated, remainingTargeters);
                return newAction.iterate(contempDone);
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
                return new RollbackMessage("Non ci sono bersagli validi disponibili da questa " +
                        "posizione");
            }

            // available target or no target
            if (thisTargeter.y.automatic)
                return choiceMaker.pick(0); // In automatic targeters 0 picks the first valid
            else
                return new PickTargetMessage(choiceMaker, sandbox);
        } else if (!availableContemp.isEmpty()){
            /*
            1. Chiedi che azione
            2. Esegui l'azione
            3. Chiama l'azione corrente ma con iterate(true)
             */
            Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerMessage> finalizerContemp =
                    tupRes -> {
                        Sandbox sandboxInner = tupRes.x;
                        Map<String, Targetable> targsInner = tupRes.y;
                        Action actionAfterCont = Action.spawn(Action.this, targsInner, List.of());
                        actionAfterCont = Action.spawn(actionAfterCont, sandboxInner,
                                actionAfterCont.unresolvedEffects);

                        return actionAfterCont.iterate(true);
                    };



            ActionPicker pickAction = new ActionPicker() {
                @Override
                public Tuple<Boolean, List<ActionView>> showActionsAvailable() {
                    return new Tuple<>(true,
                            availableContemp.stream().map(ActionTemplate::generateView).collect(Collectors.toList()));
                }

                @Override
                public ControllerMessage pickAction(int choice) {
                    if (choice<0){
                        return finalizerContemp.apply(new Tuple<>(sandbox, previousTargets));
                    } else {
                        ActionTemplate actionTemplate = availableContemp.get(choice);
                        return actionTemplate.generate(sandbox, previousTargets,
                                finalizerContemp).iterate();
                    }
                }
            };

            return new PickActionMessage(pickAction, "Scegli un'azione da effettuare in " +
                    "contemporanea a quella precedente", sandbox, List.of());

        } else if (!unresolvedEffects.isEmpty()){

            Iterator<EffectTemplate> unresolvedIter= unresolvedEffects.iterator();

            // Retrieve first element and remove it from unresolvediter
            EffectTemplate nextEffect = unresolvedIter.next();

            Function<Sandbox, ControllerMessage> fun = sandbox1 -> {
                List<EffectTemplate> unresolvedList = new ArrayList<>();
                unresolvedIter.forEachRemaining(unresolvedList::add); // Add all effects except
                // the first one to the list
                Action newAction = Action.spawn(this, sandbox1, unresolvedList);
                return newAction.iterate(contempDone);
            };

            return nextEffect.spawn(previousTargets, sandbox, fun);
        }
        else return finalizer.apply(new Tuple<>(sandbox, previousTargets));
    }
}
