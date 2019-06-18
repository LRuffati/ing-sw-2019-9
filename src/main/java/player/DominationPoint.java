package player;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.DominationPointTarget;
import board.Tile;
import gamemanager.DominationMode;

public class DominationPoint extends Pawn {
    private final Actor actor;
    private final Tile tile;

    //TODO: what to do for the actor field
    DominationPoint(Actor actor, Tile tile, DominationMode scoreboard){
        scoreboard.addTrack(tile.getColor());
        this.tile = tile;
        this.actor = actor;
    }

    /**
     * @return a DominationPointTarget
     */
    @Override
    public BasicTarget targetFactory() {
        return new DominationPointTarget(damageableUID);
    }
}
