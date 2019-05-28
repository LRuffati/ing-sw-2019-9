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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
                AmmoAmountUncapped tot = new AmmoAmountUncapped(new AmmoAmount());
                for(int i: choice){
                    tot.add(scariche.get(i).x);
                }
                if (new AmmoAmountUncapped(sandbox.updatedAmmoAvailable)<tot){
                    return new ControllerActionResultServer(ActionResultType.REDO, "Not enough ammo");
                } else {
                    // TODO: make it update weapons
                    return consumer.apply(new Sandbox(sandbox, List.of(new PayEffect(tot))));
                }
            }
        };
        return new ControllerActionResultServer(chooser);
    }
}
