package player;
import uid.DamageableUID;
import uid.TileUID;

/**
 * This class implements a playable character in the game. Every pawn in the game is bound to a player and every player
 * to a single pawn. The pawn represents the player in the map and it is the damageable and movable component of every
 * participant.
 */

public class Pawn {
    private TileUID tileUID;
    private DamageableUID damageableUID;
    private Player p;

    /**
     * The constructor will assign, from the respective classes, a Tile identifier and a Damageable identifier defined
     * as UID.
     */
    public Pawn(){
        this.tileUID = new TileUID();
        this.damageableUID = new DamageableUID();
    }

    /**
     * The method set a bound between an unbounded player and the pawn.
     * @param player must be unbounded, otherwise it will throw an AlreadyBoundedPlayer exception.
     */
    //TODO Write the AlreadyBoundedPlayer exception.
    //TODO Check what happens if player is already bound to another pawn.
    protected void setBinding(Player player){
        if(p == null) this.p = player;
    }

    /**
     *
     */
}
