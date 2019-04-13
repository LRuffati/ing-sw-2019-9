package actions;

import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import board.Direction;
import player.Actor;
import uid.PowerUpUID;
import board.Tile;
import uid.TileUID;

import java.util.List;

public class PowerUp {
    private PowerUpType type;
    private PowerUpUID powerUpUID;
    private AmmoColor ammo;

    public PowerUp(PowerUpType t, AmmoColor c){
        this.type = t;
        this.ammo = c;
        this.powerUpUID = new PowerUpUID();

    }

    public void useTeleporter(Actor a, Tile t) {
        //Todo: make compatible with just using UIDs
        // if (a.isTurn()) a.movePlayer(t);
    }
    public void useTeleporter(Actor a, TileUID t){
        if (a.isTurn()) a.getPawn().move(t);
    }

    public void useTagbackGranade(Actor a, Actor target){
        //TODO check if a has just been attacked and a method to mark a target.
     }

     public void useNewton(Actor a, Actor target, Direction dir){
        //if (a.isTurn()) target.movePlayer(target.getPawn().getTile().getNeighbor(true, dir)); //Correction to getNeighbor needed.
     }

     public void useTargettingScope(Actor a, List<Actor> targets){
        //TODO attack action needed.
     }


}
