package grabbables;

import actions.effects.Effect;
import actions.utils.AmmoAmount;
import actions.utils.PowerUpType;
import board.Tile;
import exception.AmmoException;
import player.Actor;
import testcontroller.controllermessage.ControllerMessage;
import uid.TileUID;
import viewclasses.PowerUpView;

import java.util.List;
import java.util.Set;

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
     * @return true if the effect can be used, false otherwise
     */
    public abstract boolean canUse(List<Effect> lastEffects);

    /**
     *
     * @param pov the actor using the powerup (if this not in pov.powerups then onPowerupFinalized)
     * @param lastEffects effects prior to this powerup (used for targeting scope)
     * @param onPowerupFinalized the function to call once the effects have been merged in Main
     * @return the controller message to show the user
     */
    public abstract ControllerMessage usePowup(Actor pov,
                                               List<Effect> lastEffects,
                                               Runnable onPowerupFinalized);

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

    public TileUID spawnLocation(Set<Tile> tiles){
        for(Tile tile:tiles){
            try {
                if(tile.getColor().equals(ammo.getOnlyColor())) return tile.getTileID();
            } catch (AmmoException e) {
                System.out.println("ammo è vuota");
            }
        }
        return null;
    }

}

class PUFactory{
    public static PowerUp powerUpFactory(PowerUpType type, AmmoAmount ammo){
        //TODO
    }
}