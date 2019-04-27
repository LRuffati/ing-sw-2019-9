package actions;

import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import board.Direction;
import uid.PowerUpUID;
import uid.TileUID;

public class PowerUp {
    private PowerUpType type;
    private PowerUpUID powerUpUID;
    private AmmoColor ammo;

    public PowerUp(PowerUpType t, AmmoColor c){
        this.type = t;
        this.ammo = c;
        this.powerUpUID = new PowerUpUID();

    }

    public void useTeleporter(Actor actor, TileUID tile) throws NoSuchFieldException {
        if (actor.isTurn())
            actor.move(tile);
    }

    public void useTagbackGranade(Actor a, Actor target){
        //TODO check if a has just been attacked and a method to mark a target.
     }

     public void useNewton(Actor a, Actor target, Direction dir){
        //if (a.isTurn()) target.movePlayer(target.getPawn().getTile().getNeighbor(true, dir)); //Correction to getNeighbor needed.
     }

     public void useTargettingScope(Actor actor, Actor target){
        //TODO attack action needed.
     }


}
