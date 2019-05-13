package actions.effects;

import actions.WeaponUse;
import actions.targeters.targets.Targetable;
import actions.utils.WeaponChooser;
import board.Sandbox;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import grabbables.Weapon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Fire implements EffectTemplate {

    public Fire(){
        // All the information needed is passed through function parameters
    }

    @Override
    public ControllerActionResult spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox, ControllerActionResult> consumer) {
        List<Tuple<Boolean, Weapon>> allWeap = sandbox.getArsenal();
        List<Weapon> loadedWeapon =
                allWeap.stream().filter(i->i.x).map(i->i.y).collect(Collectors.toList());
        WeaponChooser chooser = new WeaponChooser() {
            @Override
            public List<Weapon> showOptions() {
                //TODO convertire a view
                return new ArrayList<>(loadedWeapon);
            }

            @Override
            public ControllerActionResult pick(int[] choice){
                if (choice[0] < 0 || choice[0] >= loadedWeapon.size()){
                    return new ControllerActionResult(this);
                } else {
                    Weapon weapUsed = loadedWeapon.get(choice[0]);
                    List<Effect> effects = List.of(new Effect() {
                        @Override
                        public Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons) {
                            Map<Weapon, Boolean> updated = new HashMap<>(oldWeapons);
                            updated.put(weapUsed, Boolean.FALSE);
                            return updated;
                        }
                    });
                    Sandbox newSandbox = new Sandbox(sandbox, effects);

                    WeaponUse action = new WeaponUse(weapUsed,newSandbox, consumer);
                    return new ControllerActionResult(action);
                }
            }
        };
        return new ControllerActionResult(chooser);
    }
}
