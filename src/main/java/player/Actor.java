package player;

import actions.PowerUp;
import board.GameMap;
import grabbables.Weapon;
import board.Tile;
import uid.DamageableUID;
import uid.GrabbableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Optional;

/**
 * The class Actor implements the status of the player in the game.
 */

public class Actor {
    private int points;
    private int numOfDeaths;
    private Dictionary<DamageableUID, Integer> marks;
    private Collection<Weapon> weapons;
    private Collection<PowerUp> powerups;
    //private AmmoAmount ammoAviable;                   //cannot use AmmoAmount, need something else
    private boolean startingPlayerMarker;
    private Pawn pawn;
    private Boolean frenzy;
    //private ActionBoard actionBoard;
    private Boolean turn;
    private GameMap gm;

    /**
     * The constructor assigns null points and deaths counter and bind a new pawn to the player. It checks if it's the
     * starting player.
     */
    public Actor(GameMap map){
        this.points = 0;
        this.numOfDeaths = 0;
        this.pawn = new Pawn();
        pawn.setBinding(this);
        this.startingPlayerMarker = false; //TODO I need to check in some way if any other player is already the first.
        this.weapons = null; //TODO Weapon constructor.
        this.frenzy = false;
        this.marks = null;
        this.gm = map;
    }

    /**
     * This method implements the first phase of a player turn.
     * Check if the player can move to the selected tile: check direction, actual tile neighbors ecc.
     * @param t is the Tile id where the player is trying to move to.
     */
    public void movePlayer(TileUID t, int steps){
        if(steps >= 0 && ((frenzy && steps <=4) || steps <= 3)&& gm.getTile(t).getSurroundings(false, steps).contains(t)){
            pawn.move(t);
        }
    }

    /**
     * Check if the player is in the same tile as the grabbable item.
     * Check if the item is weapon, then if weapons' inventory is full.
     * If it is then makes the player choose what weapon discard.
     * @param item is the grabbable picked up by the player.
     */
    public void pickUp(GrabbableUID item, Optional<TileUID> tileToMove, Optional<Weapon> wToRemove){
        //TODO check validity.
        //if(tileToMove.isPresent() && steps >= 0 && ((frenzy && steps <=4) || steps <= 3)&& gm.getTile(t).getSurroundings(false, steps).contains(t))
        Tile pos = gm.getTile(this.pawn.getTile());
        Collection<GrabbableUID> gr = pos.getGrabbable();
        if(gr.contains(item)){
            if(weapons.size() >= 3 && wToRemove.isPresent()){
                removeWeapon(wToRemove.get());
            }
            pos.pickUpGrabbable(item);
            //weapon.add(item);
        }
    }

    /**
     * Check if the weapon is owned by the player, then remove it permanently from the actual game.
     * @param w is the weapon to be discarded.
     */
    public void removeWeapon(Weapon w){
        if(weapons.contains(w)) weapons.remove(w);
    }

    /**
     * Check if the weapon is owned by the player, if the player owns enough ammo and then reloads the weapon.
     * @param w is the weapon to be reloaded.
     */
    /*public void reloadWeapon(Weapon w){
        if(weapons.contains(w)){
            w.reloaded = true;
            //TODO Ammo management.
        }
    }
    */
    /**
     * Add to the @points attribute the points gain from a kill.
     */
    public void score(){
        //TODO
    }

    /**
     *
     * @return the pawn bound to the player.
     */
    public Pawn getPawn() {
        return pawn;
    }

    /**
     *
     * @return true is the actor turn has started and it's not finished yet.
     */
    public boolean isTurn(){
        return turn;
    }


}
