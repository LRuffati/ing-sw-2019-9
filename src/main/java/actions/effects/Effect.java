package actions.effects;

import actions.utils.AmmoAmountUncapped;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashMap;
import java.util.Map;

public interface Effect {
    default Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons){
        return new HashMap<>(oldWeapons);
    }

    default Map<DamageableUID, TileUID> newLocations(Map<DamageableUID, TileUID> old){
        return new HashMap<>(old);
    }

    default AmmoAmountUncapped newAmmoAvailable(AmmoAmountUncapped old){
        return old;
    }

}
