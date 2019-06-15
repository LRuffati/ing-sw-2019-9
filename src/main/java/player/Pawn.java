package player;
import board.GameMap;
import grabbables.PowerUp;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    //TODO: addColor
    private Color color = new Color(0,0,0);
    private String username = "";

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


    /**
     * This method sets the color of the Pawn
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * This method sets the name of the Pawn
     * @param username the name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ActorView generateView(GameMapView gameMapView, boolean pointOfView) {

        ActorView actorView = new ActorView();
        for(ActorView a : gameMapView.players())
            if(a.uid().equals(damageableUID))
                actorView = a;


        List<ActorView> damageTaken = new ArrayList<>();
        for(Actor a : getActor().getDamageTaken()){
            damageTaken.add(getActorView(gameMapView, a));
        }
        actorView.setDamageTaken(damageTaken);

        Map<ActorView, Integer> marks = new HashMap<>();
        for(Map.Entry entry: getActor().getMarks().entrySet()){
            marks.put(getActorView(gameMapView, (DamageableUID)entry.getKey()), (Integer)entry.getValue());
        }
        actorView.setMarks(marks);


        actorView.setColor(color);
        actorView.setUsername(username);

        actorView.setNumOfDeaths(getActor().getNumOfDeaths());
        actorView.setAmmo(getActor().getAmmo());
        actorView.setFirstPlayer(getActor().getFirstPlayer());

        actorView.setUnloadedWeapon(getActor().getUnloadedWeapon().stream().map(Weapon::generateView).collect(Collectors.toCollection(ArrayList::new)));
        if(pointOfView){
            actorView.setLoadedWeapon(getActor().getLoadedWeapon().stream().map(Weapon::generateView).collect(Collectors.toCollection(ArrayList::new)));
            actorView.setPowerUp(getActor().getPowerUp().stream().map(PowerUp::generateView).collect(Collectors.toCollection(ArrayList::new)));
            actorView.setScore(getActor().getPoints());
        }
        else {
            actorView.setLoadedWeapon(null);
            actorView.setPowerUp(null);
            actorView.setScore(-1);
        }

        return actorView;
    }


    private ActorView getActorView(GameMapView gameMapView, DamageableUID pawn){
        return getActorView(gameMapView, map.getPawn(pawn).getActor());
    }
    private ActorView getActorView(GameMapView gameMapView, Actor actor){
        for(ActorView actorView : gameMapView.players()) {
            if (actor.pawnID().equals(actorView.uid())) {
                return actorView;
            }
        }
        return null;
    }
}
