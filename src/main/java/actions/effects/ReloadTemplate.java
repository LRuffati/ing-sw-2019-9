package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.WeaponChooser;
import board.Sandbox;
import genericitems.Tuple;
import grabbables.Weapon;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickWeaponMessage;
import controller.controllermessage.RollbackMessage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReloadTemplate implements EffectTemplate{

    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox,
                                   Function<Sandbox, ControllerMessage> consumer){
        //1. Genera WeaponChooser

        List<Tuple<AmmoAmount, Weapon>> scariche =
                sandbox.getArsenal()
                        .stream()
                        .filter(i->!i.x)
                        .map(i-> new Tuple<>(i.y.getReloadCost(), i.y))
                        .collect(Collectors.toList());

        WeaponChooser chooser = new WeaponChooser() {

            /**
             * @return x=True if choice is optional
             * y=True if I have to choose just one
             */
            @Override
            public Tuple<Boolean, Boolean> params() {
                return new Tuple<>(true, false);
            }

            @Override
            public List<Weapon> showOptions() {
                return scariche.stream().map(i->i.y).collect(Collectors.toList());
            }

            @Override
            public ControllerMessage pick(List<Integer> choice) {
                AmmoAmountUncapped tot = new AmmoAmount();
                List<Effect> reloaded = new ArrayList<>();
                if (choice.isEmpty()){
                    return consumer.apply(sandbox);
                }
                for(int i: choice){
                    tot = tot.add(scariche.get(i).x);
                    reloaded.add(new ReloadEffect(scariche.get(i).y));
                }
                if (!new AmmoAmountUncapped(sandbox.getUpdatedTotalAmmoAvailable().getAmounts()).canBuy(tot)){
                    return new RollbackMessage("Not enough ammo");
                } else {
                    //1. Create sandbox with reloads effect
                    Sandbox reloadedSandbox = new Sandbox(sandbox, reloaded);
                    EffectTemplate payTemplate = new PayTemplate(tot);
                    return payTemplate.spawn(targets, reloadedSandbox, consumer);
                }
            }
        };
        return new PickWeaponMessage(chooser, "Seleziona le armi da ricaricare", sandbox);
    }
}

