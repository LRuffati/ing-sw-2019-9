package player;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.DominationPointTarget;
import board.Tile;
import gamemanager.DominationMode;

public class DominationPoint extends Pawn {
    private final Tile tile;

    //TODO: what to do for the actor field

    //TODO: create a domination actor,

    DominationPoint(Tile tile, DominationMode scoreboard){
        scoreboard.addTrack(tile.getColor());
        this.tile = tile;
        super.actor = actor;
    }

    /**
     * @return a DominationPointTarget
     */
    @Override
    public BasicTarget targetFactory() {
        return new DominationPointTarget(damageableUID);
    }
}
