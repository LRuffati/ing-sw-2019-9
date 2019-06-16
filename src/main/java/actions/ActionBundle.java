package actions;

import actions.effects.Effect;
import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import board.Sandbox;
import genericitems.Tuple;
import testcontroller.controllermessage.*;
import viewclasses.ActionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ActionBundle implements ActionPicker {
    private boolean finalized;
    private final List<ActionTemplate> actionsPossible;
    private final Sandbox sandboxFromMap;

    private Function<Tuple<Sandbox, Map<String, Targetable>>,
            ControllerMessage> finalizer;

    public ActionBundle(Sandbox sandbox, List<ActionTemplate> actions,
                        Function<List<Effect>, ControllerMessage> finalize){

        this.actionsPossible = actions;

        finalized = false;
        this.sandboxFromMap = sandbox;

        finalizer = tup -> {
            Sandbox sandboxReturned = tup.x;

            List<Effect> effectsHistory = sandboxReturned.getEffectsHistory();

            if (this.isFinalized())
                return new WaitMessage(List.of());
            else {
                // Function to confirm
                Function<Integer, ControllerMessage> confirmation =
                        integer -> {
                            if (!integer.equals(0)){
                                return new RollbackMessage("Effettua i cambiamenti desiderati");
                            }
                            this.finalized = true;
                            return finalize.apply(effectsHistory);
                        };

                List<String> pass = new ArrayList<>();
                pass.add(0, "Sì confermo l'azione");
                pass.add(1, "No, voglio modificare qualcosa");
                return new StringChoiceMessage(pass, "Vuoi confermare l'azione o modificare",
                        confirmation, sandboxReturned);
            }
        };
    }

    @Override
    public Tuple<Boolean, List<ActionView>> showActionsAvailable() {
        return new Tuple<>(false, actionsPossible.stream().map(ActionTemplate::generateView).collect(Collectors.toList()));
    }

    @Override
    public ControllerMessage pickAction(int choice) {
        if (choice<0){
            return new PickActionMessage(this, "Devi effettuare un'azione", sandboxFromMap,List.of());
        }
        Sandbox sandbox = new Sandbox(sandboxFromMap, List.of());
        ActionTemplate chosen = actionsPossible.get(choice);
        Action action = new Action(sandbox, chosen, Map.of("self", sandbox.getBasic(sandbox.pov)),
                finalizer);
        return action.iterate();
    }

    public boolean isFinalized(){
        return finalized;
    }
}
