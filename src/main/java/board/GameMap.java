package board;
import java.util.*;
import uid.TileUID;
import uid.RoomUID;
/**
 * The logical container of all elements of the map, room, tile, pawns, munition cards and grabbable weapons cards
 */
public class GameMap {

    /**
     * Default constructor
     */
    public GameMap() {
        //TODO
    }

    /**
     *  Map between RoomUID and the Room Class
     */
    private Map<RoomUID,Room> roomUIDMap;

    /**
     *  Map between TileUID and the Tile Class
     */
    private Map<TileUID, Tile> tileUIDMap;

    /**
     * Stores the absolute position of each Tile. Should not be used to access to TileUID
     */
    private Map<Coord, TileUID> position;

    /** Return the Tile Object given a TileUID
     * @param tileID TileUID of the wanted Tile
     * @return the Tile corresponding to the TileUID. If no Tile is found, an empty Optional is returned
     */
    protected Optional<Tile> getTile(TileUID tileID) {
        return Optional.ofNullable(tileUIDMap.get(tileID));
    }

    /** Return the Room Object given a RoomUID
     * @param roomID RoomUID of the wanted Room
     * @return the Room corresponding to the RoomUID. If no Room is found, an empty Optional is returned
     */
    protected Optional<Room> getRoom(RoomUID roomID) {
        return Optional.ofNullable(roomUIDMap.get(roomID));
    }

    /** Return the TileUID Object given a Coordinate
     * @param coord Coordinate of the wanted Tile. Upper left Tile has Coord = (0,0)
     * @return the TileUID corresponding to the Coordinate. If no Tile is found, an empty Optional is returned
     */
    protected Optional<TileUID> getPosition(Coord coord) {
        return Optional.ofNullable(position.get(coord));
    }
}