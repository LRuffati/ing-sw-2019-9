package actions.effects;

import actions.utils.AmmoAmountUncapped;
import board.GameMap;
import grabbables.Weapon;
import player.Actor;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Effect {

    EffectType type();

    default Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons){
        return new HashMap<>(oldWeapons);
    }

    default Map<DamageableUID, TileUID> newLocations(Map<DamageableUID, TileUID> old){
        return new HashMap<>(old);
    }

    default AmmoAmountUncapped newAmmoAvailable(AmmoAmountUncapped old){
        return old;
    }

    /**
     *
     * @param next
     * @param pov
     * @param gameMap
     * @param finalize
     * @return
     */
    ControllerMainLineResultServer mergeInGameMap(List<Effect> next, Actor pov, GameMap gameMap,
                                                  Runnable<ControllerMainLineResultServer> finalize)
}
