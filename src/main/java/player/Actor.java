package player;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.GameMap;
import exception.AmmoException;
import grabbables.AmmoCard;
import grabbables.Grabbable;
import grabbables.PowerUp;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;

import java.lang.invoke.WrongMethodTypeException;
import java.security.InvalidParameterException;
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
    public Actor(GameMap map){
        this.points = 0;
        this.numOfDeaths = 0;
        this.damageTaken = new ArrayList<>();
        this.pawn = new Pawn();
        pawn.setBinding(this);
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

    /**
     * Constructor needed for pawn tests.
     */
    public Actor(){
        this.pawn = null;
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
        pawn.move(tile);
    }

    /**
     * Checks if in the player's tile there is the grabbable item.
     * Check if the item is weapon, then if weapons' inventory is full.
     * Checks if the weapon chosen to be discarded is a valid weapon.
     * Picks up the weapon or the ammotile, add all the necessary ammo and powerUps
     * @param item is the grabbable element that the player want to pick up
     * @param wToRemove contains the weapon that must be discarded. If there is no need to discard weapons, this field is left unchecked
     * @throws InvalidParameterException if the parameters aren't valid
     * @throws WrongMethodTypeException if it's not the player's turn
     * @throws AmmoException if the player doesn't have enough ammo
     */
    public void pickUp(Grabbable item, Weapon wToRemove) throws AmmoException{
        TileUID tile = this.pawn.getTile();

        if(!turn)
            throw new WrongMethodTypeException("It's not your turn");
        if(!gm.getGrabbable(tile).contains(item))
            throw new InvalidParameterException("There isn't this item here");

        if(gm.getTile(tile).spawnPoint()){

            if(!checkAmmo((Weapon) item))
                throw new AmmoException("Not enough ammo available");

            if(weapons.size() >= 3) {
                if (wToRemove != null)
                    throw new InvalidParameterException("A weapon must be discarded");
                else
                    if(!weapons.contains(wToRemove))
                        throw new InvalidParameterException("You haven't this weapon");
                    else
                        gm.addGrabbable(tile, wToRemove);
                weapons.remove(wToRemove);
            }
            weapons.add((Weapon)gm.pickUpGrabbable(tile, item));
            ((Weapon)item).setLoaded();
        }
        else{
            AmmoCard card = (AmmoCard)gm.pickUpGrabbable(tile, item);
            ammoAvailable = ammoAvailable.add(card.getAmmoAmount());
            for(int i = 0; i<card.getNumOfPowerUp(); i++)
                powerups.add((PowerUp)gm.pickUpPowerUp());
            gm.discardAmmoCard(card);
        }
    }

    /**
     * Check if the weapon is owned by the player, if the player owns enough ammo and then reloads the weapon.
     * @param weapon is the weapon to be reloaded.
     * @throws AmmoException if the player doesn't have enough ammo
     */
    public void reloadWeapon(Weapon weapon) throws AmmoException{
        if(!weapons.contains(weapon)) throw new InvalidParameterException("This actor has not this weapon");
        if(!weapon.isLoaded())   throw new InvalidParameterException("This weapon is already loaded");
        if(checkAmmo(weapon))
            weapon.setLoaded();
        else
            throw new AmmoException("Not enough ammo available");
        /*
        weapon.canReload(ammoAvailable).ifPresent(ammoAvailable -> weapon.canReload(ammoAvailable));
        weapon.setLoaded();
        */
    }

    private boolean checkAmmo(Weapon weapon){
        Optional<AmmoAmount> result = weapon.canReload(ammoAvailable);
        if(result.isPresent()) {
            ammoAvailable = result.get();
            return true;
        }
        else
            return false;
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

    /**
     *
     * @return true if Frenzy turn.
     */
    public Boolean getFrenzy() {
        return frenzy;
    }

    /**
     * Needed for tests.
     * @return marks owned by the player.
     */
    public Dictionary<DamageableUID, Integer> getMarks() {
        return marks;
    }

    /**
     * Needed for tests.
     * @return the map of the game the player is playing.
     */
    public GameMap getGm() {
        return gm;
    }

    /**
     * Neede for tests.
     * @param pawn
     */
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
