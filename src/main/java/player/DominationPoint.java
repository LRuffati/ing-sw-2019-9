package player;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.DominationPointTarget;
import board.DominationPointTile;
import board.GameMap;
import board.Tile;
import uid.DamageableUID;
import uid.TileUID;

import java.awt.*;

/**
 * This class is an extension of the {@link Pawn pawn} class, and is used to contain a DominationPoint
 */
public class DominationPoint extends Pawn {
    private final DominationPointTile tile;
    private final DamageableUID uid;


    /**
     * Default constructor
     * @param uid the identifier of the pawn
     * @param tile the Tile where the pawn is
     * @param map the GameMap
     */
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

    /**
     * DominationPoint cannot move, so this method does nothing
     */
    @Override
    public void move(TileUID tile) {
        //does nothing
    }
}
