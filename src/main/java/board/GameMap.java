package board;
import java.util.Map;
import java.util.NoSuchElementException;

import uid.TileUID;
import uid.RoomUID;
/**
 * The logical container of all elements of the map, room, tile, pawns, munition cards and grabbable weapons cards
 */
public class GameMap {

    /**
     * Default constructor
     */
    public GameMap(Map<RoomUID,Room> roomUIDMap, Map<TileUID, Tile> tileUIDMap, Map<Coord, TileUID> position) {
        this.roomUIDMap = roomUIDMap;
        this.tileUIDMap = tileUIDMap;
        this.position = position;
        //TODO fare un vero costruttore
    }

    /**
     *  Map between RoomUID and the Room Class
     */
    private final Map<RoomUID,Room> roomUIDMap;

    /**
     *  Map between TileUID and the Tile Class
     */
    private final Map<TileUID, Tile> tileUIDMap;

    /**
     * Stores the absolute position of each Tile. Should not be used to access to TileUID
     */
    private final Map<Coord, TileUID> position;

    /** Return the Tile Object given a TileUID
     * @param tileID TileUID of the wanted Tile
     * @return the Tile corresponding to the TileUID
     * @exception NoSuchElementException If no Tile is found, an exception is returned
     */
    protected Tile getTile(TileUID tileID) {
        if(tileUIDMap.containsKey(tileID))
            return tileUIDMap.get(tileID);
        else
            throw new NoSuchElementException("This TileUID does not exists");
    }

    /** Return the Room Object given a RoomUID
     * @param roomID RoomUID of the wanted Room
     * @return the Room corresponding to the RoomUID
     * @exception NoSuchElementException If no Room is found, an exception is returned
     */
    protected Room getRoom(RoomUID roomID) {
        if(roomUIDMap.containsKey(roomID))
            return roomUIDMap.get(roomID);
        else
            throw new NoSuchElementException("This RoomUID does not exists");
    }

    /** Return the TileUID Object given a Coordinate
     * @param coord Coordinate of the wanted Tile. Upper left Tile has Coord = (0,0)
     * @return the TileUID corresponding to the Coordinate. If no Tile is found, an empty Optional is returned
     * @exception NoSuchElementException If no Coord is found, an exception is returned
     */
    protected TileUID getPosition(Coord coord) {
        if(position.containsKey(coord))
            return position.get(coord);
        else
            throw new NoSuchElementException("This Coord does not exists");
    }
}