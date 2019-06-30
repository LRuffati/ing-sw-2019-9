package player;
import actions.targeters.targets.BasicTarget;
import board.GameMap;
import gamemanager.ParserConfiguration;
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
    protected transient Actor actor;
    public final DamageableUID damageableUID;
    private transient GameMap map;

    private Color color = Color.yellow;
    private String colorString = "yellow";
    private String username = "";

    /**
     * The constructor will assign, from the respective classes, a Tile identifier and a
     * Damageable identifier defined as UID.
     */
    public Pawn(DamageableUID damageableUID, TileUID position, GameMap map){
        this.tile = position;
        this.damageableUID = damageableUID;
        this.map = map;
        this.actor = null;
    }

    /**
     * The method set a bound between an unbounded player and the pawn.
     */
    void setBinding(Actor player){
        if((this.actor == null) && (player.pawn().actor == null)) {
            this.actor = player;
        }
    }

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

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    /**
     * This method sets the name of the Pawn
     * @param username the name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return a string representation of the username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param gameMapView the gameMap connected to the ActorView
     * @param uid the player that is being created
     * @param pov the owner of the map
     * @return a view of the actor
     */
    public ActorView generateView(GameMapView gameMapView, DamageableUID uid, DamageableUID pov) {

        boolean pointOfView = uid.equals(pov);

        ActorView actorView = new ActorView(uid);
        for(ActorView a : gameMapView.players()) {
            if (a.uid().equals(damageableUID))
                actorView = a;
        }



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
        actorView.setColorString(colorString);
        actorView.setUsername(username);
        actorView.setHp(ParserConfiguration.parseInt("Hp"));

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
            actorView.setLoadedWeapon(List.of());
            actorView.setPowerUp(List.of());
            actorView.setScore(-1);
        }

        return actorView;
    }

    /**
     *
     * @param gameMapView
     * @param pawn
     * @return
     */
    private ActorView getActorView(GameMapView gameMapView, DamageableUID pawn){
        return getActorView(gameMapView, map.getPawn(pawn).getActor());
    }

    /**
     *
     * @param gameMapView
     * @param actor
     * @return
     */
    private ActorView getActorView(GameMapView gameMapView, Actor actor){
        for(ActorView actorView : gameMapView.players()) {
            if (actor.pawnID().equals(actorView.uid())) {
                return actorView;
            }
        }
        return null;
    }

    /**
     *
     * @return a BasicTarget
     */
    public BasicTarget targetFactory() {
        return new BasicTarget(damageableUID);
    }
}
