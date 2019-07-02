package board;

import gamemanager.DominationMode;
import gamemanager.Scoreboard;
import player.Actor;
import player.ControlPointActor;
import player.DominationPoint;
import player.Pawn;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.Map;

/**
 * This class is an extension of Tile and identify the Tiles containing Domination Points
 */
public class DominationPointTile extends Tile {

    private ControlPointActor controlPointActor;
    private final DamageableUID pawnID;

    /**
     * Default constructor
     *
     * @param map        The reference to the board
     * @param roomID     The identifier of the room
     * @param tileID     The identifier of the tile
     * @param neighbors  The neighbors of the tile
     */
    DominationPointTile(GameMap map, RoomUID roomID, TileUID tileID, Map<Direction, NeighTile> neighbors) {
        super(map, roomID, tileID, neighbors, true);
        this.pawnID = new DamageableUID();
    }

    /**
     * Updates the scoreboard such that it will contain also a track of this color
     */
    public void addTrack(Scoreboard scoreboard) {
        ((DominationMode)scoreboard).addTrack(this.getColor());
    }

    public Actor getControlPointActor() {
        return controlPointActor;
    }

    @Override
    public void endTurn(Actor actor) {
        if(map.tile(actor.pawnID()).equals(this.tileID)) {
            actor.damageRaw(controlPointActor, 1);
            controlPointActor.steppedOn(actor);
        }

    }

    @Override
    protected void setMap(GameMap map, Map<DamageableUID, Pawn> damageableUIDPawnMap) {
        super.setMap(map, damageableUIDPawnMap);
        this.controlPointActor = new ControlPointActor(map, pawnID, false, getColor());
        damageableUIDPawnMap.put(pawnID, new DominationPoint(pawnID, this, map));
    }



}
