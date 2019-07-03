package grabbables;

import actions.effects.Effect;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import board.Tile;
import controller.SetMessageProxy;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;
import uid.TileUID;
import viewclasses.PowerUpView;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that contain a powerUp.
 * This is implemented by {@link TargScope TargetingScope}, {@link Newton Newton}, {@link TagBack TagBackGrenade}, {@link Teleporter Teleporter}
 */
public abstract class PowerUp extends Grabbable {
    private final PowerUpType type;
    private final AmmoColor color;

    /**
     * Default constructor, it only sets the type of powerUp and its color
     * @param type {@link actions.utils.PowerUpType type} of the powerUp
     * @param color {@link actions.utils.AmmoColor color} of the powerUp
     */
    public PowerUp(PowerUpType type, AmmoColor color ){
        this.type = type;
        this.color = color;
    }

    /**
     * Gets the list of effects which were played prior to this call and tells if I can use the
     * powerup
     * @param lastEffects
     * @return true if the effect can be used, false otherwise
     */
    public abstract boolean canUse(List<Effect> lastEffects);

    /**
     * Uses and discards the powerup
     * @param pov the actor using the powerup (if this not in pov.powerups then onPowerupFinalized)
     * @param lastEffects effects prior to this powerup (used for targeting scope   )
     * @param onPowerupFinalized the function to call once the effects have been merged in Main
     * @return the controller message to show the use
     */
    public abstract ControllerMessage usePowup(SetMessageProxy pov,
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
        return new AmmoAmount(Map.of(color, 1));
    }

    @Override
    public String toString() {
        return getType().toString() + " " + color.name();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    /**
     * Generates a new {@link viewclasses.PowerUpView powerUpView} of this card
     * @return a PowerUpView containing all the information needed by the client
     */
    public PowerUpView generateView() {
        PowerUpView powerUpView = new PowerUpView();
        powerUpView.setAmmo(getAmmo());
        powerUpView.setType(type);
        powerUpView.setUid(super.getId());
        return powerUpView;
    }

    /**
     * Returns the proper spawnpoint from a set of spawnpoints
     * @param tiles the
     * @return
     */
    public TileUID spawnLocation(Set<Tile> tiles){
        for(Tile tile:tiles){
            if (tile.getColor().equals(color.toColor()))
                return tile.getTileID();
        }
        return null;
    }

    /**
     * Factory method that builds a new PowerUp
     * @param type {@link actions.utils.PowerUpType type} of the powerUp
     * @param color {@link actions.utils.AmmoColor color} of the powerUp
     * @return a new PowerUp
     */
    public static PowerUp powerUpFactory(PowerUpType type, AmmoColor color){
        PowerUp ret=null;
        switch (type){
            case NEWTON:
                ret = new Newton(color);
                break;
            case TELEPORTER:
                ret = new Teleporter(color);
                break;
            case TAGBACKGRANADE:
                ret = new TagBack(color);
                break;
            case TARGETINGSCOPE:
                ret = new TargScope(color);
                break;
        }
        return ret;
    }

}
