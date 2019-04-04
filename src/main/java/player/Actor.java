package player;

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
    //private Collection<PowerUps> powerups;
    //private AmmoCounter ammoAviable;
    private boolean startingPlayerMarker;
    protected Pawn pawn;
    private PlayerBoardSide playerboardside;
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
        this.playerboardside = null; //TODO board constructor;

    }

    public void movePlayer(TileUID t){
        //TODO check validity.
        pawn.move(t);
    }

    public void pickUp(Weapon w){
        //TODO check validity.
        if(weapons.size() >= 3){
            //TODO remove the last weapon
        }
        weapons.add(w);

    }


}
