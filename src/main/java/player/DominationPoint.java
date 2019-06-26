package player;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.DominationPointTarget;
import board.DominationPointTile;
import board.GameMap;
import board.Tile;
import gamemanager.DominationMode;
import uid.DamageableUID;

public class DominationPoint extends Pawn {
    private final Tile tile;
    private final DamageableUID uid;

    //TODO: what to do for the actor field

    //TODO: create a domination actor,

    public DominationPoint(DamageableUID uid, Tile tile, GameMap map){
        super(uid, tile.tileID, map);
        this.uid = uid;
        this.tile = tile;
    }

    public Tile getDominationPointTile() {
        return tile;
    }

    /**
     * @return a DominationPointTarget
     */
    @Override
    public BasicTarget targetFactory() {
        return new DominationPointTarget(damageableUID);
    }

    @Override
    public DamageableUID getDamageableUID() {
        return uid;
    }
}
