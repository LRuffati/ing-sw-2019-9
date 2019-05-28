package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.WeaponChooser;
import board.Sandbox;
import controllerresults.ActionResultType;
import controllerresults.ControllerActionResultServer;
import genericitems.Tuple;
import grabbables.Weapon;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReloadTemplate implements EffectTemplate{

    public ControllerActionResultServer spawn(Map<String, Targetable> targets, Sandbox sandbox,
                                  Function<Sandbox,
            ControllerActionResultServer> consumer){
        //1. Genera WeaponChooser

        List<Tuple<AmmoAmount, Weapon>> scariche =
                sandbox.getArsenal()
                        .stream()
                        .filter(i->!i.x)
                        .map(i-> new Tuple<>(i.y.getReloadCost(), i.y))
                        .collect(Collectors.toList());
        WeaponChooser chooser = new WeaponChooser() {

            @Override
            public List<Weapon> showOptions() {
                return scariche.stream().map(i->i.y).collect(Collectors.toList());
            }

            @Override
            public ControllerActionResultServer pick(int[] choice) {
                AmmoAmountUncapped tot = new AmmoAmount();
                List<Effect> reloaded = new ArrayList<>();
                Function<Weapon, Effect> fun = weapon -> new Effect() {
                    @Override
                    public EffectType type() {
                        return EffectType.RELOAD;
                    }

                    @Override
                    public Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons) {
                        Map<Weapon, Boolean> newW = new HashMap<>(oldWeapons);
                        newW.put(weapon, Boolean.TRUE);
                        return newW;
                    }
                };

                for(int i: choice){
                    tot.add(scariche.get(i).x);
                    reloaded.add(fun.apply(scariche.get(i).y));
                }
                if (new AmmoAmountUncapped(sandbox.updatedAmmoAvailable.getAmounts()).compareTo(tot)<0){
                    return new ControllerActionResultServer(ActionResultType.REDO, "Not enough " +
                            "ammo", sandbox);
                } else {
                    reloaded.add(new PayEffect(tot));
                    return consumer.apply(new Sandbox(sandbox, reloaded));
                }
            }
        };
        return new ControllerActionResultServer(chooser, "Pick weapons to reload", sandbox);
    }
}
