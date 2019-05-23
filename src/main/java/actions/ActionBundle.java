package actions;

import actions.effects.Effect;
import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import board.GameMap;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
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

    private List<Effect> effects;

    private final Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> finalizer;

    ActionBundle(GameMap map, List<ActionTemplate> actions, DamageableUID caller){
        this.actionsPossible = actions;
        finalized = false;
        this.map = map;
        this.pov = caller;
        finalizer = tup -> { // This will be called once the action is complete
            /*
            Objective:
                Add all the effects to ActionBundle
                Return TERMINATED
                The controller will read a Terminated, will know to look for the original
                ActionBundle and will proceed to apply to the game map, as well as getting the
                necessary info for the GRAB and PAY effects
             */
            Sandbox sandbox = tup.x;
            if (this.finalized) return new ControllerActionResult(ActionResultType.ALREADYTERMINATED);
            else {
                this.finalized = true;
                this.effects = sandbox.getEffectsHistory();
                return new ControllerActionResult(ActionResultType.TERMINATED);
            }
        };
    }


    @Override
    public Tuple<Boolean, List<ActionView>> showActionsAvailable() {
        return new Tuple<>(false, actionsPossible.stream().map(ActionTemplate::generateView).collect(Collectors.toList()));
    }

    @Override
    public ControllerActionResult pickAction(int choice) {
        if (choice<0 || choice>=actionsPossible.size()){
            return new ControllerActionResult(this);
        }
        Sandbox sandbox = map.createSandbox(pov);
        ActionTemplate chosen = actionsPossible.get(choice);
        Action action = new Action(sandbox, chosen, Map.of("self", sandbox.getBasic(pov)), finalizer);
        return action.iterate();
    }


    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
}
