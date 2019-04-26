package grabbables;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;

import java.util.Map;

public class PowerUp extends Grabbable {
    private final PowerUpType type;
    private final AmmoAmount ammo;

    public PowerUp(PowerUpType type, AmmoAmount ammo){
        this.type = type;
        this.ammo = ammo;
    }

    public PowerUpType getType() {
        return type;
    }

    public AmmoAmount getAmmo() {
        return ammo;
    }

    @Override
    public String toString() {
        String ret;
        Map<AmmoColor, Integer> amount = ammo.getAmounts();
        ret = getType().toString();
        ret += "RED:" + amount.get(AmmoColor.RED)
                +" BLUE:" + amount.get(AmmoColor.BLUE)
                +" YELLOW:" + amount.get(AmmoColor.YELLOW);
        return ret;
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
