package board;

import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Sandbox {

    Sandbox(GameMap original){
    }

    /**
     * Returns the neighbors of the cell
     * @param tile the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical){
        return null;
    }

    /**
     * Returns the tiles in the manhattan circle of radius given centered on a tile
     * If radius is 0 return a set with only the tile given
     * If less than 0 return an empty set
     * @param centre
     * @param radius
     * @param logical go through walls or not
     * @return
     */
    public HashSet<TileUID> circle(TileUID centre, int radius, boolean logical){
        HashSet<TileUID> retVal = new HashSet<>();
        HashSet<TileUID> interior = new HashSet<>();
        HashSet<TileUID> border;

        if (radius<0) return retVal;
        retVal.add(centre);
        int rad = 0;

        while (rad<radius){
            border = new HashSet<>();
            //1. Find the border
            border.addAll(retVal);
            border.removeAll(interior);
            //2. Find neighbors of the border
            for (TileUID i: border){
                retVal.addAll(neighbors(i, logical).values());
            }
            interior.addAll(border);
            rad++;
        }
        return retVal;

    }

    /**
     * Returns all the pawns (player or domination points) contained in the tile
     * @param tile
     * @return
     */
    public Collection<DamageableUID> containedPawns(TileUID tile){
        return null;
    }

    /**
     * Returns the room containing the tile
     * @param tile
     * @return
     */
    public RoomUID room(TileUID tile){
        return null;
    }

    /**
     * Returns the room containing the pawn
     * @param pawn
     * @return
     */
    public RoomUID room(DamageableUID pawn){
        return null;
    }

    /**
     * @param pawn
     * @return the room containing the pawn
     */
    public TileUID tile(DamageableUID pawn){
        return null;
    }

    /**
     *
     * @param room
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room){
        return null;
    }

    public Set<TileUID> tilesSeen(TileUID source){
        Collection<TileUID> tilesNear = neighbors(source, true).values();

        //Todo: convert to functional
        HashSet<RoomUID> roomsNeigh = new HashSet<>();
        for (TileUID i: tilesNear){
            roomsNeigh.add(room(i));
        }

        HashSet<TileUID> visible = new HashSet<>();
        for (RoomUID i: roomsNeigh){
            visible.addAll(tilesInRoom(i));
        }

        return visible;

    }
}
