package player;

import actions.PowerUp;
import grabbables.Weapon;
import board.Tile;
import uid.DamageableUID;
import uid.GrabbableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.Dictionary;

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

    /**
     * The constructor assigns null points and deaths counter and bind a new pawn to the player. It checks if it's the
     * starting player.
     */
    public Actor(){
        this.points = 0;
        this.numOfDeaths = 0;
        this.pawn = new Pawn();
        pawn.setBinding(this);
        this.startingPlayerMarker = false; //TODO I need to check in some way if any other player is already the first.
        this.weapons = null; //TODO Weapon constructor.
        this.frenzy = false;
        this.marks = null;
    }

    /**
     * Check if the player can move to the selected tile: check direction, actual tile neighbors ecc.
     * @param t is the Tile where the player is trying to move to.
     */
    public void movePlayer(TileUID t){
        //TODO check validity.
        pawn.move(t);
    }

    /**
     * Check if the player is in the same tile as the grabbable item.
     * Check if the item is weapon, then if weapons' inventory is full.
     * If it is then makes the player choose what weapon discard.
     * @param item is the grabbable picked up by the player.
     */
    public void pickUp(GrabbableUID item){
        //TODO check validity.
    }

    /**
     * Select from the weapon owned which to discard and remove from the actual game.
     */
    public void removeWeapon(){
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
