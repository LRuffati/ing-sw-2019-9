package actions.effects;

import actions.WeaponUse;
import actions.targeters.targets.Targetable;
import actions.utils.WeaponChooser;
import board.Sandbox;
import genericitems.Tuple;
import grabbables.Weapon;
import player.Actor;
import testcontroller.SlaveController;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.PickActionMessage;
import testcontroller.controllermessage.PickWeaponMessage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                //TODO convertire a view
                return new ArrayList<>(loadedWeapon);
            }

            @Override
            public ControllerMessage pick(List<Integer> choice){
                if (choice.get(0) < 0 || choice.get(0) >= loadedWeapon.size()){
                    return new PickWeaponMessage(this, "", sandbox);
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

                        /**
                         * @param pov
                         * @param finalize contains all the instructions to run after the end of the effect. Contains
                         */
                        @Override
                        public void mergeInGameMap(SlaveController pov, Runnable finalize) {
                            pov.getSelf().drop(weapUsed);
                        }

                        @Override
                        public String effectString(Actor pov) {
                            return String.format("%s ha usato %s", pov.pawn().getUsername(), weapUsed.getName());
                        }
                    });
                    Sandbox newSandbox = new Sandbox(sandbox, effects);

                    WeaponUse action = new WeaponUse(weapUsed,newSandbox, consumer);
                    return new PickActionMessage(action,"", sandbox,List.of());
                }
            }
        };
        return new PickWeaponMessage(chooser,"", sandbox);
    }
}
