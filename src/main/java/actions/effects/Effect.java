package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;
import uid.WeaponUID;

import java.util.HashMap;
import java.util.Map;

public interface Effect {
    default public Map<Weapon, Boolean> newWeapons(Map<Weapon, Boolean> oldWeapons){
        return new HashMap<>(oldWeapons);
    }

    default public Map<DamageableUID, TileUID> newLocations(Map<DamageableUID, TileUID> old){
        return new HashMap<>(old);
    }

    default public AmmoAmountUncapped newAmmoAvailable(AmmoAmountUncapped old){
        return old;
    }

}
