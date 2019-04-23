package player;

import actions.PowerUp;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.GameMap;
import grabbables.Grabbable;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;

import java.util.*;

/**
 * The class Actor implements the status of the player in the game.
 */

public class Actor {
    private int points;
    private int numOfDeaths;
    private ArrayList<Actor> damageTaken;
    private Dictionary<DamageableUID, Integer> marks;
    private Collection<Weapon> weapons;
    private Collection<PowerUp> powerups;
    private AmmoAmount ammoAvailable;
    private boolean startingPlayerMarker;
    private Pawn pawn;
    private Boolean frenzy;
    private Boolean turn;
    private transient GameMap gm;

    private DamageableUID pawnID;

    /**
     * The constructor assigns null points and deaths counter and bind a new pawn to the player.
     * It checks if it's the starting player.
     */
    public Actor(GameMap map, DamageableUID pawnId){
        this.points = 0;
        this.numOfDeaths = 0;
        this.damageTaken = new ArrayList<>();
        this.pawnID = pawnId;
        pawn().setBinding(this);
        this.startingPlayerMarker = false;
        this.weapons = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.ammoAvailable = new AmmoAmount(Map.of(AmmoColor.RED,1,AmmoColor.BLUE,1,AmmoColor.YELLOW,1));
        this.frenzy = false;
        this.marks = null;
        this.gm = map;
    }

    /**
     * This constructor gets the GameMap and the Pawn, and build the Actor
     * @param map GameMap
     * @param pawnId the Pawn identifier that has to be associated with this Actor
     * @param firstPlayer True if the Actor is the first player in the game
     */
    public Actor(GameMap map, DamageableUID pawnId, boolean firstPlayer){
        this.points = 0;
        this.numOfDeaths = 0;
        this.damageTaken = new ArrayList<>();
        this.pawnID = pawnId;
        this.startingPlayerMarker = firstPlayer;
        this.weapons = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.ammoAvailable = new AmmoAmount(Map.of(AmmoColor.RED,1,AmmoColor.BLUE,1,AmmoColor.YELLOW,1));
        this.frenzy = false;
        this.marks = null;
        this.gm = map;
    }


    public void setBinding(){
        pawn().setBinding(this);
    }

    /**
     * Provides a faster way to get the Pawn object given the identifier
     * @return The pawn
     */
    private Pawn pawn(){
        return gm.getPawn(pawnID);
    }

    /**
     * Sets the GameMap of the Actor
     * @param map The GameMap
     */
    public void setMap(GameMap map){
        gm = map;
    }

    /**
     * This method implements the first phase of a player turn.
     * Check if the player can move to the selected tile: check direction, actual tile neighbors ecc.
     * @param t is the Tile id where the player is trying to move to.
     */
    public void movePlayer(TileUID t){
        if(turn &&
                (!frenzy && gm.getSurroundings(false, 3, pawn.getTile()).contains(t))
                ||
                (frenzy && gm.getSurroundings(false, 4, pawn.getTile()).contains(t))
        ) {
            move(t);
        }
    }

    public void unconditionalMove(TileUID tile){
        move(tile);
    }

    /**
     * This method implements the basic action of movement
     * It modifies the tile of the Pawn and move the Player in the GameMap
     * @param tile the tile where the Pawn must be put
     */
    private void move(TileUID tile){
        pawn.move(tile, gm);
    }

    /**
     * Check if the player is in the same tile as the grabbable item.
     * Check if the item is weapon, then if weapons' inventory is full.
     * If it is then makes the player choose what weapon discard.
     * @param item is the grabbable picked up by the player.
     */
    public void pickUp(Grabbable item, Optional<TileUID> tileToMove, Optional<Weapon> wToRemove){
        //TODO check validity.
        if(turn){
            TileUID pos = this.pawn.getTile();
            Collection<Grabbable> gr = gm.getGrabbable(pos);
            if(gr.contains(item)) {
                if (weapons.size() >= 3 && wToRemove.isPresent()) {
                    removeWeapon(wToRemove.get());
                }
                gm.pickUpGrabbable(pos, item);
                //weapon.add(item);
            }
        }
    }

    /**
     * Check if the weapon is owned by the player, then remove it permanently from the actual game.
     * @param w is the weapon to be discarded.
     */
    public void removeWeapon(Weapon w){
        weapons.remove(w);
    }

    /**
     * Check if the weapon is owned by the player, if the player owns enough ammo and then reloads the weapon.
     * @param w is the weapon to be reloaded.
     */
    public void reloadWeapon(Weapon w){
        if(weapons.contains(w) && !w.isLoaded()){
            w.canReload(ammoAvailable).ifPresent(ammoAvailable -> w.canReload(ammoAvailable));
            w.setLoaded();
        }
    }



    /**
     *
     * @return the pawn bound to the player.
     */
    public Pawn getPawn() {
        return pawn();
    }

    /**
     *
     * @return true is the actor turn has started and it's not finished yet.
     */
    public boolean isTurn(){
        return turn;
    }

    /**
     * Needed to other class to set who's the first player playing (needed for Frenzy Final).
     */
    public void setStartingPlayerMarker() {
        this.startingPlayerMarker = true;
    }

    /**
     *
     * @return the points actually owned by the player.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Needed to end and start a player turn.
     * @param turn true to start the turn, false to end it.
     */
    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    /**
     * Add the attacker who damaged the player on his playerboard.
     * The first element is the first player who attacked "this".
     * Also converts all the marks of the shooter into damage.
     * @param shooter is the attacker.
     */
    public void getDMG(Actor shooter){
        damageTaken.add(shooter);
        for(int i=0; i<marks.get(shooter.pawnID); i++){
            if(damageTaken.size() <= 10)    damageTaken.add(shooter);
        }
        marks.put(shooter.pawnID, 0);
    }

    /**
     * Method needed in the Scoreboard class.
     * @return the damage taken from a single shot.
     */
    public ArrayList<Actor> getDamageTaken() {
        return damageTaken;
    }

    /**
     *
     * @param p is the number of points to be added to the player current points.
     */
    public void addPoints(int p){
        this.points+=p;
    }

    /**
     *
     * @return the amount of ammo ready to be used.
     */
    public AmmoAmount getAmmo() {
        return ammoAvailable;
    }

    /**
     *
     * @return the number of deaths in order to know how many points will get the player that will shoot next time.
     */
    public int getNumOfDeaths() {
        return numOfDeaths;
    }
}
