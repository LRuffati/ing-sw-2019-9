package actions;

import actions.effects.Effect;
import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import board.GameMap;
import board.Sandbox;
import genericitems.Tuple;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.PickActionMessage;
import testcontroller.controllermessage.StringChoiceMessage;
import testcontroller.controllermessage.WaitMessage;
import uid.DamageableUID;
import viewclasses.ActionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ActionBundle implements ActionPicker {
    private final DamageableUID pov;
    private boolean finalized;
    private final List<ActionTemplate> actionsPossible;
    private final GameMap map;
    private final Sandbox sandboxFromMap;

    private List<Effect> effects;

    private final Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerMessage> finalizer;

    public ActionBundle(GameMap map, List<ActionTemplate> actions, DamageableUID caller){
        this.actionsPossible = actions;
        finalized = false;
        this.map = map;
        this.pov = caller;
        this.sandboxFromMap = map.createSandbox(pov);
        finalizer = tup -> { // This will be called once the action is complete
            /*
            Check if finalized, else create a PickStringMessage and return it
            */
            Sandbox sandbox = tup.x;
            if (this.finalized) return new WaitMessage();
            else {
                Function<Integer, ControllerMessage> func = i -> {
                    this.finalized = true;
                    this.effects = sandbox.getEffectsHistory();

                    //TODO: write the actual function
                    /*
                    1. Setta lo slaveController in modalità wait
                    2. Fa' partire un nuovo thread
                    3. Restituisce Wait alla rete

                    Il thread:
                    1. Passa la lista di effetti al MainController e termina, se il MainController
                    restituisce un false allora:
                        1. Ripulisce gli effect in action bundle
                        2. Resetta la flag finalized
                        3. Resetta controller timer
                        4. Rimette in slavecontroller il ControllerMessage precedente
                     */

                    return new WaitMessage();
                };
            List<String> pass = new ArrayList<>();
            pass.add(0, "Sì confermo l'azione");
            pass.add(1, "No, voglio modificare qualcosa");
            return new StringChoiceMessage(pass, "Vuoi confermare l'azione o modificare", func);
        }

        };
    }


    @Override
    public Tuple<Boolean, List<ActionView>> showActionsAvailable() {
        return new Tuple<>(false, actionsPossible.stream().map(ActionTemplate::generateView).collect(Collectors.toList()));
    }

    @Override
    public ControllerMessage pickAction(int choice) {
        if (choice<0 || choice>=actionsPossible.size()){
            return new PickActionMessage(this, "", sandboxFromMap);
        }
        Sandbox sandbox = map.createSandbox(pov);
        ActionTemplate chosen = actionsPossible.get(choice);
        Action action = new Action(sandbox, chosen, Map.of("self", sandbox.getBasic(pov)), finalizer);
        return action.iterate();
    }

    public boolean isFinalized(){
        return finalized;
    }

    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
}
