package player;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.DominationPointTarget;
import board.DominationPointTile;
import board.GameMap;
import board.Tile;
import uid.DamageableUID;

import java.awt.*;

public class DominationPoint extends Pawn {
    private final DominationPointTile tile;
    private final DamageableUID uid;

    //TODO: what to do for the actor field

    public DominationPoint(DamageableUID uid, Tile tile, GameMap map){
        super(uid, tile.tileID, map);
        this.uid = uid;
        this.tile = (DominationPointTile)tile;

        this.setColor(Color.darkGray);
        this.setUsername("Domination Point");
    }

    public DominationPointTile getDominationPointTile() {
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
