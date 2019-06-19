package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.WeaponChooser;
import board.Sandbox;
import exception.AmmoException;
import genericitems.Tuple;
import grabbables.Weapon;
import player.Actor;
import testcontroller.SlaveController;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.PickWeaponMessage;
import testcontroller.controllermessage.RollbackMessage;

import java.util.*;
import java.util.function.Consumer;
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
                    tot.add(scariche.get(i).x);
                    reloaded.add(new ReloadEffect(scariche.get(i).y));
                }
                if (new AmmoAmountUncapped(sandbox.getUpdatedTotalAmmoAvailable().getAmounts()).compareTo(tot)<0){
                    return new RollbackMessage("Not enough ammo");
                } else {
                    //1. Create sandbox with reloads effect
                    Sandbox reloadedSandbox = new Sandbox(sandbox, reloaded);
                    EffectTemplate payTemplate = new PayTemplate(tot);
                    return payTemplate.spawn(targets, sandbox, consumer::apply);

                }
            }
        };
        return new PickWeaponMessage(chooser, "Pick weapons to reload", sandbox);
    }
}

class ReloadEffect implements Effect{

    private final Weapon weapon;

    ReloadEffect(Weapon weapon){
        this.weapon = weapon;
    }

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

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {

    }

    @Override
    public String effectString(Actor pov) {
        return String.format("%s ha ricaricato %s",
                pov.pawn().getUsername(),
                weapon.getName()
        );
    }
}