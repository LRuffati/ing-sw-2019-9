package player;

import actions.AmmoAmount;
import actions.PowerUp;
import actions.Weapon;
import uid.TileUID;

import java.util.Collection;

/**
 * The class Actor implements the status of the player in the game.
 */

public class Actor {
    private int points;
    private int numOfDeaths;
    //private Dictionary<> marks;
    private Collection<Weapon> weapons;
    private Collection<PowerUp> powerups;
    //private AmmoAmount ammoAviable;                   //cannot use AmmoAmount, need something else
    private boolean startingPlayerMarker;
    private Pawn pawn;
    private Boolean frenzy;
    //private ActionBoard actionBoard;

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
    }

    public void movePlayer(TileUID t){
        //TODO check validity.
        pawn.move(t);
    }

    /**
     * Check if the player is in the same tile as the weapon.
     * Check if the weapons' inventory is full. If it is then makes the player choose what weapon discard.
     * @param w is the weapon picked up by the player.
     */
    public void pickUp(Weapon w){
        //TODO check validity.
        if(weapons.size() >= 3){
            removeWeapon();
        }
        weapons.add(w);
    }

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


}
