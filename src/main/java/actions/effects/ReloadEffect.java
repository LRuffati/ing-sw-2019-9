package actions.effects;

import grabbables.Weapon;
import player.Actor;
import controller.SlaveController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster){
        try {
            pov.getSelf().reloadWeapon(weapon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        broadcaster.accept(effectString(pov.getSelf()));
    }

    String effectString(Actor pov) {
        return String.format("%s ha ricaricato %s",
                pov.pawn().getUsername(),
                weapon.getName()
        );
    }
}
