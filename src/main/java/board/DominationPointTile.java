package board;

import player.Actor;
import player.ControlPointActor;
import player.DominationPoint;
import player.Pawn;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.Map;

public class DominationPointTile extends Tile {

    private final Actor controlPointActor;
    DamageableUID pawnID;

    /**
     * Default constructor
     *
     * @param map        The reference to the board
     * @param roomID     The identifier of the room
     * @param tileID     The identifier of the tile
     * @param neighbors  The neighbors of the tile
     */
    public DominationPointTile(GameMap map, RoomUID roomID, TileUID tileID, Map<Direction, NeighTile> neighbors) {
        super(map, roomID, tileID, neighbors, true);
        this.controlPointActor = new ControlPointActor();
    }

    @Override
    public void endTurn(Actor actor) {
    }
}
