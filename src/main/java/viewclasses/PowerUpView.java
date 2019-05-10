package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;

import java.io.Serializable;
import java.util.Map;

/**
 * This class contains the PowerUp card that is used by the view and transmitted from the server to the client
 */
public class PowerUpView implements Serializable {
    private AmmoColor ammo;
    private PowerUpType type;

    public void setAmmo(AmmoAmount ammo) {
        for(Map.Entry entry : ammo.getAmounts().entrySet())
            if((Integer)entry.getValue() != 0)
                this.ammo = (AmmoColor)entry.getKey();
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public PowerUpType type() {
        return type;
    }

    public AmmoColor ammo() {
        return ammo;
    }
}
