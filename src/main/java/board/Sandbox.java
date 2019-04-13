package board;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.TileTarget;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.*;
import java.util.stream.Collectors;

public class Sandbox {

    private final Map<RoomUID, RoomTarget> rooms;
    private final Map<TileUID, TileTarget> tiles;
    private final Map<DamageableUID, BasicTarget> pawns;
    private final GameMap map;

    Sandbox(Map<RoomUID, RoomTarget> rooms, Map<TileUID, TileTarget> tiles, Map<DamageableUID, BasicTarget> pawns, GameMap map){

        this.rooms = rooms;
        this.tiles = tiles;
        this.pawns = pawns.entrySet()
                .stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BasicTarget.basicFactory(this, e.getValue())));
        this.map = map;
    }

    /**
     * Returns the neighbors of the cell
     * @param tile the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical){
        return map.neighbors(tile, logical);
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
    public Set<TileUID> circle(TileUID centre, int radius, boolean logical){
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
        return pawns.entrySet()
                .stream().filter(i -> i.getValue().location() == tile).map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns the room containing the tile
     * @param tile
     * @return
     */
    public RoomUID room(TileUID tile){
        return map.room(tile);
    }

    /**
     * Returns the room containing the pawn
     * @param pawn
     * @return
     */
    public RoomUID room(DamageableUID pawn){
        return map.room(pawns.get(pawn).location());
    }

    /**
     * @param pawn
     * @return the room containing the pawn
     */
    public TileUID tile(DamageableUID pawn){
        return pawns.get(pawn).location();
    }

    /**
     *
     * @param room
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room){
        return map.tilesInRoom(room);
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

    public Set<TileUID> allTiles(){
        return new HashSet<>(tiles.keySet());
    }

    public RoomTarget getRoom(RoomUID roomUID) {
        return rooms.get(roomUID);
    }

    public TileTarget getTile(TileUID tileUID) { return  tiles.get(tileUID);}

    public BasicTarget getBasic(DamageableUID targetUID){ return pawns.get(targetUID);}
}
