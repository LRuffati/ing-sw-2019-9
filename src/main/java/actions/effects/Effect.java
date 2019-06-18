package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import grabbables.PowerUp;
import grabbables.Weapon;
import player.Actor;
import testcontroller.SlaveController;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Effect {

    EffectType type();

    /**
     * Sandbox updating method
     * @param oldWeapons
     * @return
     */
    default Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons){
        return new HashMap<>(oldWeapons);
    }

    /**
     * Sandbox updating
     * @param old
     * @return
     */
    default Map<DamageableUID, TileUID> newLocations(Map<DamageableUID, TileUID> old){
        return new HashMap<>(old);
    }

    /**
     * Sandbox updating
     * @param old the old amount of ammo available to the player, includes powerups
     * @return the updated amount
     */
    default AmmoAmount newAmmoAvailable(AmmoAmount old){
        return old;
    }

    /**
     *
     * @param pov
     * @param finalize contains all the instructions to run after the end of the effect. Contains
     *                also the list of effects still to apply
     */
    void mergeInGameMap(SlaveController pov,
                        Runnable finalize);

    String effectString(Actor pov);

    /**
     * Used for paying and just for paying
     * @param old the powerups used prior to this effect
     * @return old with the just used powerup added
     */
    default List<PowerUp> newUsedPowUp(List<PowerUp> old){
        return old;
    }
}
