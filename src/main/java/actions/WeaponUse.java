package actions;

import actions.targeters.targets.Targetable;
import actions.utils.ActionPicker;
import actions.utils.AmmoAmount;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import grabbables.Weapon;
import viewclasses.ActionView;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeaponUse implements ActionPicker {
    private Weapon weapon;
    private Sandbox sandbox;

    private final Function<Sandbox, ControllerActionResult> finalizer;

    Map<String, Targetable> existingTargets;
    List<String> previousActions;
    List<Tuple<String, ActionTemplate>> availableActions;

    private final boolean canStop;

    public WeaponUse(Weapon weapon, Sandbox sandbox,
                     Function<Sandbox, ControllerActionResult> finalizer){
        this.weapon = weapon;
        this.sandbox = sandbox;
        this.finalizer = finalizer;

        previousActions = new LinkedList<>();
        existingTargets = new HashMap<>();
        existingTargets.put("self", sandbox.getBasic(sandbox.pov));

        availableActions =
                weapon.getActions().entrySet().stream().filter(i->i.getValue()
                        .actionAvailable(existingTargets
                                ,previousActions,
                                new AmmoAmount(sandbox.updatedAmmoAvailable.getAmounts())))
                        .map(e -> new Tuple<>(e.getKey(),e.getValue())).collect(Collectors.toList());
        canStop = false;

    }

    public WeaponUse(WeaponUse previous, Sandbox sandboxNew,
                     Map<String, Targetable> existingTargets, List<String> previousActions){
        //TODO: gestire i contemporary
        finalizer = previous.finalizer;
        sandbox = sandboxNew;
        this.existingTargets = existingTargets;
        this.previousActions = previousActions;
        this.weapon = previous.weapon;

        availableActions =
                weapon.getActions().entrySet().stream().filter(i->i.getValue()
                        .actionAvailable(existingTargets
                                ,previousActions,
                                new AmmoAmount(sandbox.updatedAmmoAvailable.getAmounts())))
                        .map(e -> new Tuple<>(e.getKey(),e.getValue())).collect(Collectors.toList());

        canStop = availableActions.stream().noneMatch(i->i.x.equals("main"));
    }

    @Override
    public Tuple<Boolean, List<ActionView>> showActionsAvailable() {
        // I can stop only if I can't choose the main action anymore (already taken/took
        // alternative)
        List<ActionView> actionTemplates =
                availableActions.stream().map(i->i.y.generateView()).collect(Collectors.toList());
        return new Tuple<>(canStop,actionTemplates);
    }

    @Override
    public ControllerActionResult pickAction(int choice) {
        Tuple<String, ActionTemplate> action;
        if (choice<0 && canStop){
            return finalizer.apply(sandbox);
        } else if (0<=choice && choice<availableActions.size()){
            action = availableActions.get(choice);
        } else return new ControllerActionResult(ActionResultType.ROLLBACK);

        final List<String> updatedActions = new LinkedList<>(previousActions);
        updatedActions.add(action.x);
        Function<Tuple<Sandbox, Map<String, Targetable>>, ControllerActionResult> fun =
                tup -> {
                    WeaponUse weaponUseNew= new WeaponUse(WeaponUse.this, tup.x, tup.y,
                            updatedActions);
                    return new ControllerActionResult(weaponUseNew);
                };
        Action nextAction = new Action(sandbox, action.y, existingTargets, fun);
        return nextAction.iterate();
    }
}
