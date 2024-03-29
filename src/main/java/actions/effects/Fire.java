package actions.effects;

import actions.WeaponUse;
import actions.targeters.targets.Targetable;
import actions.utils.WeaponChooser;
import board.Sandbox;
import genericitems.Tuple;
import grabbables.Weapon;
import player.Actor;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;
import controller.controllermessage.PickActionMessage;
import controller.controllermessage.PickWeaponMessage;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This effect is to be used when you want to fire a weapon during the turn.
 * It makes you choose the weapon you want to use, changes the sandbox to make it unavailable and
 * then hands the process over to the WeaponUse
 */
public class Fire implements EffectTemplate {

    public Fire(){
        // All the information needed is passed through function parameters
    }

    @Override
    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer) {
        List<Tuple<Boolean, Weapon>> allWeap = sandbox.getArsenal();
        List<Weapon> loadedWeapon =
                allWeap.stream().filter(i->i.x).map(i->i.y).collect(Collectors.toList());
        WeaponChooser chooser = new WeaponChooser() {
            /**
             * @return x=True if choice is optional
             * y=True if I have to choose just one
             */
            @Override
            public Tuple<Boolean, Boolean> params() {
                return new Tuple<>(false, true);
            }

            @Override
            public List<Weapon> showOptions() {
                return new ArrayList<>(loadedWeapon);
            }

            @Override
            public ControllerMessage pick(List<Integer> choice){
                if (choice.get(0) < 0 || choice.get(0) >= loadedWeapon.size()){
                    return new PickWeaponMessage(this, "Scegli un'arma da usare", sandbox);
                } else {
                    Weapon weapUsed = loadedWeapon.get(choice.get(0));
                    List<Effect> effects = List.of(new Effect() {
                        @Override
                        public EffectType type(){
                            return EffectType.FIRE;
                        }

                        @Override
                        public Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons) {
                            Map<Weapon, Boolean> updated = new HashMap<>(oldWeapons);
                            updated.put(weapUsed, Boolean.FALSE);
                            return updated;
                        }

                        @Override
                        public void mergeInGameMap(SlaveController pov, Runnable finalize,
                                                   Consumer<String> broadcaster) {
                            pov.getSelf().useWeapon(weapUsed);
                            broadcaster.accept(effectString(pov.getSelf()));
                            new Thread(()->finalize.run()).start();
                            return;
                        }

                        String effectString(Actor pov) {
                            return String.format("%s ha usato %s", pov.pawn().getUsername(), weapUsed.getName());
                        }
                    });
                    Sandbox newSandbox = new Sandbox(sandbox, effects);

                    WeaponUse action = new WeaponUse(weapUsed,newSandbox, consumer);
                    return new PickActionMessage(action,"Scegli un'azione da eseguire", newSandbox,
                            List.of());
                }
            }
        };
        return new PickWeaponMessage(chooser,"Scegli un'arma da usare", sandbox);
    }
}
