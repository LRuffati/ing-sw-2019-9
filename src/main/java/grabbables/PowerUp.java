package grabbables;

import actions.effects.Effect;
import actions.utils.AmmoAmount;
import actions.utils.PowerUpType;
import viewclasses.PowerUpView;

import java.util.List;

public abstract class PowerUp extends Grabbable {
    private final PowerUpType type;
    private final AmmoAmount ammo;

    public PowerUp(PowerUpType type, AmmoAmount ammo){
        this.type = type;
        this.ammo = ammo;
    }

    /**
     * Gets the list of effects which were played prior to this call and tells if I can use the
     * powerup
     * @param lastEffects
     * @return
     */
    public abstract boolean canUse(List<Effect> lastEffects);

    /**
     * @return The type of powerUp contained
     */
    public PowerUpType getType() {
        return type;
    }

    /**
     * @return The type of Ammo equivalent to the PowerUp
     */
    public AmmoAmount getAmmo() {
        return ammo;
    }

    @Override
    public String toString() {
        return getType().toString() + ammo.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    public PowerUpView generateView() {
        PowerUpView powerUpView = new PowerUpView();
        powerUpView.setAmmo(ammo);
        powerUpView.setType(type);
        powerUpView.setUid(super.getId());
        return powerUpView;
    }
}
