package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import uid.GrabbableUID;

import java.io.Serializable;
import java.util.Map;

/**
 * This class contains all the information of {@link grabbables.PowerUp powerUps} needed by the client.
 * It only contains getters and setters
 */
public class PowerUpView implements Serializable {
    private AmmoColor ammo;
    private PowerUpType type;
    private GrabbableUID uid;

    public void setAmmo(AmmoAmount ammo) {
        for(Map.Entry entry : ammo.getAmounts().entrySet())
            if((Integer)entry.getValue() != 0)
                this.ammo = (AmmoColor)entry.getKey();
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    public void setUid(GrabbableUID uid) {
        this.uid = uid;
    }

    public PowerUpType type() {
        return type;
    }

    public AmmoColor ammo() {
        return ammo;
    }

    public GrabbableUID uid() {
        return uid;
    }
}
