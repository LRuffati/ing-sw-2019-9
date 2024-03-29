package player;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.GameMap;
import board.Tile;
import gamemanager.ParserConfiguration;
import gamemanager.Scoreboard;
import grabbables.AmmoCard;
import grabbables.PowerUp;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The class Actor implements the status of the player in the game.
 */

public class Actor {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    // Proprietà di game info
    private static final int HP = ParserConfiguration.parseInt("Hp");
    private static final int MAX_WEAPON = ParserConfiguration.parseInt("maxNumOfWeapon");
    private static final int MAX_PUP = ParserConfiguration.parseInt("maxNumOfPowerUp");

    // Di Actor
    private boolean lastInFrenzy;
    private int points;
    private int numOfDeaths;
    private ArrayList<Actor> damageTaken;
    private Map<DamageableUID, Integer> marks;
    private Collection<Weapon> unloadedWeapon;
    private Collection<Weapon> loadedWeapon;
    private Collection<PowerUp> powerUps; // FIXME ensure it is always updated
    private AmmoAmount ammoAvailable;
    private boolean startingPlayerMarker;

    private GameMap gm;

    private DamageableUID pawnID;

    //TODO: if resolveEffect checks this and clears it I could handle tagbacks in a more coherent
    // way, could also remove the need for slave attribute
    private Set<Actor> damagedBy;

    public Set<Actor> getDamagedBy() {
        return new HashSet<>(damagedBy);
    }

    public boolean removeDamager(Actor damager){
       return damagedBy.remove(damager);
    }


    // Frenzy related
    private Boolean frenzy;
    private boolean flipBoard = false;
    private boolean afterFirst;

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
        this.loadedWeapon = new ArrayList<>();
        this.unloadedWeapon = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.ammoAvailable = new AmmoAmount(Map.of(AmmoColor.RED,1,AmmoColor.BLUE,1,AmmoColor.YELLOW,1));
        this.frenzy = false;
        this.marks = new HashMap<>();
        this.gm = map;

        this.damagedBy = new HashSet<>();

        this.lastInFrenzy = false;
    }

    /**
     * Provide the binding between the Actor and the DamageableUID
     */
    public void setBinding(){
        pawn().setBinding(this);
    }

    /**
     * Provides a faster way to get the Pawn object given the identifier
     * @return The pawn
     */
    public Pawn pawn(){
        return gm.getPawn(pawnID);
    }

    /**
     * This method implements the basic action of movement
     * It modifies the tile of the Pawn and move the Player in the GameMap
     * @param tile the tile where the Pawn must be put
     */
    public void move(TileUID tile) {
        pawn().move(tile);
    }

    /**
     * This method discards a powerUp without checking if the player own the card
     * @param powerUp the powerUp that need to be discarded
     */
    public void discardPowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
        gm.discardPowerUp(powerUp);
        logger.log(Level.INFO, () -> "discardPowerUp " + powerUp.toString());

    }

    /**
     * This metohd picks up a certain amount of power Ups, without check of validity
     * @param num the number of powerUp
     */
    public void drawPowerUpRaw(int num) {
        for(int i=0; i<num; i++)
            powerUps.add(gm.pickUpPowerUp());
    }

    /**
     * Checks if in the player's tile there is the grabbable item.
     * Picks up the weapon or the ammoTile, adds all the necessary ammo and powerUps
     * @param card is the ammoCard element that the player want to pick up
     */
    public void pickUp(AmmoCard card){
        TileUID tile = pawn().getTile();
        if(!gm.getGrabbable(tile).contains(card))
            throw new InvalidParameterException("There isn't this item here");

        gm.pickUpGrabbable(tile, card); //Removes from the map
        ammoAvailable = new AmmoAmount(ammoAvailable.add(new AmmoAmount(card.getAmmoAmount())));
        for(int i = 0; i<card.getNumOfPowerUp() && powerUps.size()< MAX_PUP; i++)
            powerUps.add(gm.pickUpPowerUp());
        gm.discardAmmoCard(card);
    }

    /**
     * Checks if in the player's tile there is the weapon.
     * Check if weapons' inventory is full.
     * Checks if the weapon chosen to be discarded is a valid weapon.
     * Picks up the weapon and discard all the weapon and powerUps
     * @param weapon is the weapon that the player want to pick up
     * @param weaponToDiscard contains the weapon that must be discarded. If there is no need to discard weapons, this field is left unchecked
     */
    public void pickUp(Weapon weapon, Optional<Weapon> weaponToDiscard){
        TileUID tile = pawn().getTile();
        if(!gm.getGrabbable(tile).contains(weapon))
            throw new InvalidParameterException("There isn't this item here");

        if((loadedWeapon.size() + unloadedWeapon.size()) >= MAX_WEAPON) {
            if(weaponToDiscard.isEmpty() || !weaponToDiscard.isPresent()) {
                throw new InvalidParameterException("A weapon must be discarded");
            }

            Weapon weaponToDisc = weaponToDiscard.get();

            if(!(loadedWeapon.contains(weaponToDisc) || unloadedWeapon.contains(weaponToDisc)))
                throw new InvalidParameterException("You haven't this weapon");

            gm.discardWeapon(tile, weaponToDisc);
            if (loadedWeapon.contains(weaponToDisc))
                loadedWeapon.remove(weaponToDisc);
            else
                unloadedWeapon.remove(weaponToDisc);
        }
        gm.pickUpGrabbable(tile,weapon);
        loadedWeapon.add(weapon);
    }

    /**
     * Check if the weapon is owned by the player, if the player owns enough ammo and then reloads the weapon.
     * @param weapon is the weapon to be reloaded.
     */
    public void reloadWeapon(Weapon weapon) {
        if(!unloadedWeapon.contains(weapon) && !loadedWeapon.contains(weapon))
            throw new InvalidParameterException("This actor has not this weapon");
        if(!unloadedWeapon.contains(weapon))
            throw new InvalidParameterException("This weapon is already loaded");

        unloadedWeapon.remove(weapon);
        loadedWeapon.add(weapon);
    }

    /**
     * Once a weapon is used, it is removed from the loadedSet and added to the unloadedSet
     * @param weapon the weapon that has been used
     */
    public void useWeapon(Weapon weapon){
        loadedWeapon.remove(weapon);
        unloadedWeapon.add(weapon);
    }

    /**
     * Check if the powerup is owned by the player and discard it
     * @param p the powerup
     */
    public void pay(PowerUp p){
        if (getPowerUp().contains(p)){
            discardPowerUp(p);
        }
    }

    /**
     * Pay the given amount using the ammocubes
     * @param amount the amount to be paid
     */
    public void pay(AmmoAmount amount){
        ammoAvailable = new AmmoAmount(ammoAvailable.subtract(amount));
    }


    /**
     *
     * @return the points actually owned by the player.
     */
    public int getPoints() {
        return points;
    }


    /**
     * Damages the player without applying the Marks nor triggering the tagback
     *
     * Also used to apply the damage
     *
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    public void damageRaw(Actor shooter, int numOfDmg) {
        System.out.println("damage added to "+this.pawn().getUsername());
        for(int i=0; i<numOfDmg; i++){
            if(damageTaken.size() <= HP){
                damageTaken.add(shooter);
            }
        }
    }

    /**
     * Damages the player, and applies all the Marks that can be applied, triggers tagback on
     * next check
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    public void damage(Actor shooter, int numOfDmg){
        damagedBy.add(shooter);

        Integer toApply = marks.remove(shooter.pawnID);
        if (toApply==null)
            toApply = 0;

        damageRaw(shooter,numOfDmg+toApply);
    }

    //
    // See T O D O above for damage related effects
    //

    /**
     * Method needed in the Scoreboard class.
     * @return the damage taken from a single shot.
     */
    public List<Actor> getDamageTaken() {
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
    public Map<DamageableUID, Integer> getMarks() {
        return Map.copyOf(marks);
    }

    /**
     * Needed for tests.
     * @return the map of the game the player is playing.
     */
    public GameMap getGm() {
        return gm;
    }

    /**
     *
     * @return True iif the Actor is the player that started the game
     */
    public boolean getFirstPlayer(){
        return startingPlayerMarker;
    }

    /**
     *
     * @return True iif the player is dead
     */
    public boolean isDead(){
        return damageTaken.size()>=HP;
    }

    /**
     * Adds a certain number of marks from pawn to this.
     * If the pawn already assigned 3 marks nothing happens.
     * @param attackerActor The attacker
     * @param numOfMarks the number of marks to apply
     * @return the number of marks successfully applied
     */
    public int addMark(Actor attackerActor, int numOfMarks){

        DamageableUID attackerPawn = attackerActor.pawnID;

        int totMarks = marks.getOrDefault(attackerPawn, 0);

        // Minimum between numOfMarks and 3 - totmarks
        int applied = Math.min(numOfMarks , 3 - totMarks);

        marks.put(attackerPawn, marks.getOrDefault(attackerPawn, 0) + applied);

        return applied;
    }

    /**
     *
     * @return how many marks this Actor applied to other Actors
     */
    public int numOfMarksApplied(){
        int totMarks = 0;
        for(DamageableUID p : gm.getDamageable()){
            if(gm.getPawn(p).getActor().getMarks().containsKey(this.pawnID))
                totMarks += gm.getPawn(p).getActor().getMarks().get(this.pawnID());
        }
        return totMarks;
    }

    /**
     * This function resets the player after a death, the spawn location has been already
     * determined and is handled by the calling process
     */
    public void respawn(PowerUp powerUp){
       Set<Tile> spawns = getGm().allTiles().stream()
                                    .map(getGm()::getTile)
                                    .filter(Tile::spawnPoint)
                                    .collect(Collectors.toSet());
        TileUID destination = powerUp.spawnLocation(spawns);
        discardPowerUp(powerUp);
        pawn().move(destination);
        if (frenzy)
            flipBoard=true;

        this.damageTaken.clear();
    }

    /**
     *
     * @return the ID of the pawn associated with the actor
     */
    public DamageableUID pawnID() {
        return pawnID;
    }


    /**
     *
     * @return A collection containing all the Loaded Weapons held by the player
     */
    public Collection<Weapon> getLoadedWeapon() {
        return loadedWeapon;
    }

    /**
     *
     * @return A collection containing all the Unoaded Weapons held by the player
     */
    public Collection<Weapon> getUnloadedWeapon() {
        return unloadedWeapon;
    }

    /**
     *
     * @return A collection containing all the PowerUps held by the player
     */
    public Collection<PowerUp> getPowerUp() {
        return new ArrayList<>(powerUps);
    }



    /**
     * Takes into account the situation of the player
     * @return
     */
    public List<List<ActionTemplate>> getActions() {
        if (!frenzy){
            return ActionTemplateOptions.getActionsStandard(damageTaken.size());
        } else {
            return ActionTemplateOptions.getFrenzyActions(!afterFirst);
        }
    }

    public void setLastInFrenzy() {
        this.lastInFrenzy = true;
    }

    public boolean isLastInFrenzy() {
        return lastInFrenzy;
    }

    /**
     * Called when FF phase is starting
     * If possible it flips the board and changes the action that can be performed
     * @param afterFirst True iif the player is between the first player and the player who started FF
     */
    public void beginFF(boolean afterFirst){
        if(damageTaken.isEmpty())
            this.flipBoard = true;

        frenzy = true;
        this.afterFirst = afterFirst;
    }

    /**
     * Manages the end of the turn. If the player is not dead nothing happens.
     * Otherwise the kill and the score are added to the scoreboard and damage are cleared.
     * If the player is dead and FF has started then flips the board
     * @param player The player that finished the turn
     * @param scoreboard Scoreboard
     * @return True iif the player needs to respawn
     */
    public boolean endTurn(Actor player, Scoreboard scoreboard) {
        if (!isDead())
            return false;

        scoreboard.addKill(player, this);
        scoreboard.score(this);
        damageTaken.clear();

        if (frenzy)
            flipBoard = true;

        return true;
    }
}

