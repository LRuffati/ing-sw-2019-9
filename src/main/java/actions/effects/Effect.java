package actions.effects;

import actions.utils.AmmoAmount;
import grabbables.PowerUp;
import grabbables.Weapon;
import testcontroller.SlaveController;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Effect {

    /**
     *
     * @return the type of the effect
     */
    EffectType type();

    /**
     * Sandbox updating method
     * @param oldWeapons the weapons previously owned by the player
     * @return a new updated weaponMap
     */
    default Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons){
        return new HashMap<>(oldWeapons);
    }

    /**
     * @param old the old location map
     * @return an updated location map
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
     * @param pov the SlaveController linked to the player enacting the effect
     * @param finalize contains all the instructions to run after the end of the effect. Including
     *                also the list of effects still to apply. Will be just called, not in a thread
     */
    void mergeInGameMap(SlaveController pov,
                        Runnable finalize,
                        Consumer<String> broadcaster);

    /**
     * Used for paying and just for paying
     * @param old the powerups used prior to this effect
     * @return old with the just used powerup added
     */
    default List<PowerUp> newUsedPowUp(List<PowerUp> old){
        return old;
    }
}
