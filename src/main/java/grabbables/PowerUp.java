package grabbables;

import actions.utils.AmmoAmount;
import actions.utils.PowerUpType;

public class PowerUp extends Grabbable {
    private PowerUpType type;
    private AmmoAmount ammo;

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
}
