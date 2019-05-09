package player;
import board.GameMap;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * This class implements a playable character in the game. Every pawn in the game is bound to a player and every player
 * to a single pawn. The pawn represents the player in the map and it is the damageable and movable component of every
 * participant.
 */

public class Pawn {
    private TileUID tile;
    private transient Actor actor;
    public final DamageableUID damageableUID;
    private transient GameMap map;
    private Color color;

    /**
     * The constructor will assign, from the respective classes, a Tile identifier and a Damageable identifier defined
     * as UID.
     */
    public Pawn(DamageableUID damageableUID, TileUID position, GameMap map){
        this.tile = position;
        this.damageableUID = damageableUID;
        this.map = map;
        this.actor = null;
    }

    /**
     * Constructor used for DominationPoint class.
     */
    public Pawn(){
        this.damageableUID = new DamageableUID();
        this.map = null;
    }

    /**
     * The method set a bound between an unbounded player and the pawn.
     * @param player must be unbounded, otherwise it will throw an AlreadyBoundedPlayer exception.
     */
    void setBinding(Actor player){
        if(this.actor == null && player.getPawn().actor == null) {
            this.actor = player;
        }
    }

    /*
    protected void setBinding(Actor player) throws AlreadyBoundedActorException{
        if(player.getPawn().actor != null){
            throw new AlreadyBoundedActorException("thie actor is already bounded");
        }
        if(actor == null && player.getPawn().actor == null) this.actor = player;
    }
    */

    /**
     * To move the pawn in a selected tile.
     * @param tile is the position where the pawn will be moved.
     */
    public void move(TileUID tile) {
        if (map != null) {
            this.tile = tile;
        } else {
            throw new InvalidParameterException("Tile not present in the map.");
        }
    }

    /**
     *
     * @return the Tile where the pawn is located.
     */
    public TileUID getTile() {
        return tile;
    }

    /**
     *
     * @return the actor bound to the pawn.
     */
    public Actor getActor() {
        return actor;
    }

    /**
     *
     * @return the pawn ID.
     */
    public DamageableUID getDamageableUID() {
        return damageableUID;
    }

    /**
     * Needed for tests.
     * @return the whole map where the pawn is placed.
     */
    public GameMap getMap() {
        return map;
    }

    public void setNullMap(){
        map = null;
    }



    public ActorView generateView() {
        ActorView actorView = new ActorView();
        actorView.setColor(color);

        //TODO: setName, setColor
        //TODO: how to manage cyclic references?????
        actorView.setDamageTaken(List.of(new ActorView()));

        return actorView;
    }
}
