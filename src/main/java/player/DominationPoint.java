package player;

import board.Tile;
import gamemanager.DominationMode;

public class DominationPoint extends Pawn {
//TODO: what to do for the actor field
    DominationPoint(Actor actor, Tile tile, DominationMode scoreboard){
        scoreboard.addTrack(tile.getColor());
    }
}
