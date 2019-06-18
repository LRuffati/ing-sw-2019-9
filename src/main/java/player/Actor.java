package player;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.AmmoColor;
import board.GameMap;
import exception.AmmoException;
import gamemanager.ParserConfiguration;
import gamemanager.Scoreboard;
import genericitems.Tuple3;
import grabbables.AmmoCard;
import grabbables.PowerUp;
import grabbables.Weapon;
import uid.DamageableUID;
import uid.TileUID;

import java.awt.*;
import java.lang.invoke.WrongMethodTypeException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;

/**
 * The class Actor implements the status of the player in the game.
 */

public class Actor {
    private static final int HP = ParserConfiguration.parseInt("Hp");
    private static final int MAX_WEAPON = ParserConfiguration.parseInt("maxNumOfWeapon");
    private static final int MAX_PUP = ParserConfiguration.parseInt("maxNumOfPowerUp");
    private boolean lastInFrenzy;
    private int points;
    private int numOfDeaths;
    private ArrayList<Actor> damageTaken;
    private Map<DamageableUID, Integer> marks;
    private Collection<Weapon> unloadedWeapon;
    private Collection<Weapon> loadedWeapon;
    private Collection<PowerUp> powerUps;
    private AmmoAmount ammoAvailable;
    private boolean startingPlayerMarker;
    private Boolean frenzy;
    private Boolean turn;
    private final GameMap gm;

    private DamageableUID pawnID;

    private Set<Actor> damagedPlayer;
    //TODO: is this necessary?
    private Set<Actor> damagedBy;
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
        this.damagedPlayer = new HashSet<>();

        this.lastInFrenzy = false;

        this.turn = false;
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
    }

    /**
     * This metohd picks up a certain amount of power Ups, without check of validity
     * @param num the number of powerUp
     * @param num the number of powerUp
     */
    public void drawPowerUpRaw(int num) {
        for(int i=0; i<num; i++)
            powerUps.add(gm.pickUpPowerUp());
    }

    /**
     * Checks if in the player's tile there is the grabbable item.
     * Picks up the weapon or the ammoTile, adds all the necessary ammo and powerUps
     * @param ammoCard is the ammoCard element that the player want to pick up
     */
    public void pickUp(AmmoCard ammoCard){
        TileUID tile = pawn().getTile();
        if(!gm.getGrabbable(tile).contains(ammoCard))
            throw new InvalidParameterException("There isn't this item here");

        AmmoCard card = (AmmoCard)gm.pickUpGrabbable(tile, ammoCard);
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
    public void pickUp(Weapon weapon, Weapon weaponToDiscard){
        TileUID tile = pawn().getTile();
        if(!turn)
            throw new WrongMethodTypeException("It's not your turn");
        if(!gm.getGrabbable(tile).contains(weapon))
            throw new InvalidParameterException("There isn't this item here");

        if((loadedWeapon.size() + unloadedWeapon.size()) >= MAX_WEAPON) {
            if(weaponToDiscard == null)
                throw new InvalidParameterException("A weapon must be discarded");
            if(!(loadedWeapon.contains(weaponToDiscard) || unloadedWeapon.contains(weaponToDiscard)))
                throw new InvalidParameterException("You haven't this weapon");

            gm.discardWeapon(tile, weaponToDiscard);
            if (loadedWeapon.contains(weaponToDiscard))
                loadedWeapon.remove(weaponToDiscard);
            else
                unloadedWeapon.remove(weaponToDiscard);
        }

        loadedWeapon.add((Weapon)gm.pickUpGrabbable(tile, weapon));
    }

    /**
     * Check if the weapon is owned by the player, if the player owns enough ammo and then reloads the weapon.
     * @param weapon is the weapon to be reloaded.
     * @throws AmmoException if the player doesn't have enough ammo
     */
    public void reloadWeapon(Weapon weapon) throws AmmoException{
        if(!unloadedWeapon.contains(weapon) && !loadedWeapon.contains(weapon))
            throw new InvalidParameterException("This actor has not this weapon");
        if(!unloadedWeapon.contains(weapon))
            throw new InvalidParameterException("This weapon is already loaded");

        unloadedWeapon.remove(weapon);
        loadedWeapon.add(weapon);
    }

    /**
     * This method checks if a certain amount of Ammo can be reached using a list of PowerUps and player's AmmoTile
     * @param ammoAmount The amount of ammo that has to be reached
     * @param powerUpToPay the powerUps that the have to be considered in the check
     * @return True if can be reloaded, false otherwise
     */
    public boolean checkAmmo(AmmoAmountUncapped ammoAmount, List<PowerUp> powerUpToPay){
        AmmoAmountUncapped amount = new AmmoAmountUncapped();
        if(powerUpToPay != null)
            for(PowerUp p : powerUpToPay){
                amount = amount.add(p.getAmmo());
            }
        amount.add(ammoAvailable);

        return ammoAmount.compareTo(amount)>0;
    }

    /**
     * This method keeps track of PowerUp cards possibly being used as ammunition
     * @return the sum of ammoAvailable and all the powerups
     */
    public AmmoAmountUncapped getTotalAmmo(){
        AmmoAmountUncapped am = new AmmoAmountUncapped(ammoAvailable.getAmounts());
        for(PowerUp pu : powerUps){
            am.add(pu.getAmmo());
        }
        return am;
    }

    /**
     * Check if the powerup is owned by the player and discard it
     * @param p the powerup
     */
    public void pay(PowerUp p){
        if (getPowerUp().contains(p)){
            gm.discardPowerUp(p);
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
     * Damages the player without applying the Marks
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    public void damageRaw(Actor shooter, int numOfDmg) {
        for(int i=0; i<numOfDmg; i++){
            if(damageTaken.size() <= HP){
                damagedBy.add(shooter);
                shooter.damagedPlayer.add(this);
                damageTaken.add(shooter);
            }
        }
    }

    /**
     * Damages the player, and applies all the Marks that can be applied
     * @param shooter the attacker
     * @param numOfDmg number of damage points
     */
    public void damage(Actor shooter, int numOfDmg){
        for(int i=0; i<numOfDmg; i++){
            getDMG(shooter);
        }
    }

    public void damageBreaking(Actor shooter, int numOfDmg, Runnable onResolved){
        //TODO
    }

    /**
     * Add the attacker who damaged the player on his playerboard.
     * Adds the shooter to the attacked's damagedBy Set
     * and the shot to the attacker's damagedPlayer Set.
     * The first element is the first player who attacked "this".
     * Also converts all the marks of the shooter into damage.
     * @param shooter is the attacker.
     */
    private void getDMG(Actor shooter){
        if(damageTaken.size() <= HP){
            damagedBy.add(shooter);
            shooter.damagedPlayer.add(this);
            damageTaken.add(shooter);
        }

        if(marks.containsKey(shooter.pawn().getDamageableUID())) {
            for (int i = 0; i < marks.get(shooter.pawnID); i++) {
                if (damageTaken.size() <= HP)
                    damageTaken.add(shooter);
            }
            marks.put(shooter.pawnID, 0);
        }
    }


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
     * @return the number of damage that an actor can take before the death
     */
    public int hp() {
        return HP;
    }

    /**
     *
     * @return The set containing all the Player that damaged the Player before his turn
     */
    public Set<Actor> getDamagedBy() {
        return new HashSet<>(damagedBy);
    }

    /**
     *
     * @return a Set containing all the Player that have been damaged by this during his turn
     */
    public Set<Actor> getDamagedPlayer() {
        return new HashSet<>(damagedBy);
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
     * @param attackerPawn The attacker
     * @param numOfMarks
     * @return the number of marks successfully applied
     */
    //TODO: convert this to addMark(Actor attackerActor, int numOfMarks) ?
    public int addMark(DamageableUID attackerPawn, int numOfMarks){
        int totMarks = gm.getPawn(attackerPawn).getActor().numOfMarksApplied();

        int applied = Math.min(totMarks + numOfMarks , 3) - totMarks;

        if(marks.containsKey(attackerPawn))
            marks.put(attackerPawn, marks.get(attackerPawn) + applied);
        else
            marks.put(attackerPawn, applied);
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
     * This method initialize the Actor when there is need to respawn.
     * It sets the new Tile and resets the damageTaken field.
     * @param color Where the player wants to respawn. It is the color of the discarded PowerUp
     * @throws InvalidParameterException if the actor is not dead.
     */
    public void respawn(AmmoColor color){
        if (frenzy)
            flipBoard=true;

        if(pawn().getTile() != gm.getEmptyTile() && !isDead())
            throw new InvalidParameterException("The player is not dead");

        for (TileUID t : gm.allTiles()){
            Color colTile = gm.getTile(t).getColor();
            Tuple3<Integer, Integer, Integer> colAmmo = color.toRGB();

            if(gm.getTile(t).spawnPoint() &&
                    colTile.getRed() == colAmmo.x
                    && colTile.getGreen() == colAmmo.y
                    && colTile.getBlue() == colAmmo.z){
                move(t);
            }
        }
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
        return powerUps;
    }



    /**
     * Takes into account the situation of the player
     * @return
     */
    public List<List<ActionTemplate>> getActions() {
        if (!frenzy){
            return ActionTemplateOptions.getActionsStandard(damageTaken.size());
        } else {
            return ActionTemplateOptions.getFrenzyActions(!afterFirst, flipBoard,
                    damageTaken.size());
        }
    }

    public void setLastInFrenzy() {
        this.lastInFrenzy = true;
    }

    public boolean isLastInFrenzy() {
        return lastInFrenzy;
    }

    public void beginFF(boolean afterFirst){
        if(damageTaken.isEmpty())
            this.flipBoard = true;

        frenzy = true;
        this.afterFirst = afterFirst;
    }

    public boolean endTurn(Actor player, Scoreboard scoreboard){
        if(player.isDead()){
            scoreboard.score(player);
            //TODO don't know if using respawn() method and if check for final frenzy
            return true;
        }
        return false;
    }

    public void drop(Weapon weapUsed) {
        //TODO
    }

}

