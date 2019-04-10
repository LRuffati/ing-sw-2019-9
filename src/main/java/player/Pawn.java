package player;
import board.Tile;
import uid.DamageableUID;
import uid.TileUID;

/**
 * This class implements a playable character in the game. Every pawn in the game is bound to a player and every player
 * to a single pawn. The pawn represents the player in the map and it is the damageable and movable component of every
 * participant.
 */

public class Pawn {
    private TileUID tile;
    public final DamageableUID damageableUID;
    private Actor actor;

    /**
     * The constructor will assign, from the respective classes, a Tile identifier and a Damageable identifier defined
     * as UID.
     */
    public Pawn(Actor actor, DamageableUID damageableUID, TileUID position){
        this.tile = position;
        this.damageableUID = damageableUID;
        this.actor = actor;
    }

    public Pawn(){
        this.damageableUID = new DamageableUID();
    }

    /**
     * The method set a bound between an unbounded player and the pawn.
     * @param player must be unbounded, otherwise it will throw an AlreadyBoundedPlayer exception.
     */
    //TODO Write the AlreadyBoundedActor exception.
    protected void setBinding(Actor player){
        if(actor == null && player.getPawn().actor == null) this.actor = player;
    }

    /**
     * To move the pawn in a selected tile.
     * @param t is the position where the pawn will be moved.
     */
    public void move(TileUID t){
       this.tile = t;
    }

    /**
     *
     * @return the Tile where the pawn is located.
     */
    public TileUID getTile() {
        return tile;
    }

    /**
     * To remove the pawn from the map when the player is dead.
     */
    public void removeFromMap(){
        this.tile = null;
    }

    public Actor getActor() {
        return actor;
    }
}
