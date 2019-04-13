package board;
import java.util.*;
import java.util.stream.Collectors;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.TileTarget;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;
import uid.RoomUID;
/**
 * The logical container of all elements of the map, room, tile, pawns, munition cards and grabbable weapons cards
 */
public class GameMap {
    //TODO Damageable statico, non modificabili
    //TODO Spostare funzioni di Tile in GameMap

    /**
     * Default constructor
     */
    public GameMap(Map<RoomUID,Room> roomUIDMap,
                   Map<TileUID, Tile> tileUIDMap,
                   Map<Coord, TileUID> position,
                   Map<DamageableUID, Pawn> damageableUIDMap) {
        //TODO fare un vero costruttore
        this.roomUIDMap = roomUIDMap;
        this.tileUIDMap = tileUIDMap;
        this.position = position;
        this.damageableUIDMap = damageableUIDMap;
    }

    public Sandbox createSandbox(){
        Map<RoomUID, RoomTarget> targetRooms=  roomUIDMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new RoomTarget(e.getKey())));

        Map<TileUID, TileTarget> targetTiles = tileUIDMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new TileTarget(e.getKey())));

        Map<DamageableUID, BasicTarget> targetPawns = damageableUIDMap.entrySet()
                .stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BasicTarget.basicFactory(e.getValue())));

        return new Sandbox(targetRooms,targetTiles,targetPawns, this);
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

    /**
     * Map between DamageableUID and Damageable Class
     */
    private Map<DamageableUID, Pawn> damageableUIDMap;


    /** Returns the Tile Object given a TileUID
     * @param tileID TileUID of the wanted Tile
     * @return the Tile corresponding to the TileUID
     * @exception NoSuchElementException If no Tile is found, an exception is returned
     */
    public Tile getTile(TileUID tileID) {
        if(tileUIDMap.containsKey(tileID))
            return tileUIDMap.get(tileID);
        else
            throw new NoSuchElementException("This TileUID does not exists");
    }

    /** Returns the Room Object given a RoomUID
     * @param roomID RoomUID of the wanted Room
     * @return the Room corresponding to the RoomUID
     * @exception NoSuchElementException If no Room is found, an exception is returned
     */
    public Room getRoom(RoomUID roomID) {
        if(roomUIDMap.containsKey(roomID))
            return roomUIDMap.get(roomID);
        else
            throw new NoSuchElementException("This RoomUID does not exists");
    }

    /** Returns the Damageable Object given a DamageableUID
     * @param damageableUID DamageableUID of the wanted Damageable
     * @return the Damageable corresponding to the DamageableUID
     * @exception NoSuchElementException If no Damageable is found, an exception is returned
     */
    public Pawn getDamageable(DamageableUID damageableUID) {
        if(damageableUIDMap.containsKey(damageableUID))
            return damageableUIDMap.get(damageableUID);
        else
            throw new NoSuchElementException("This RoomUID does not exists");
    }

    /** Returns the TileUID Object given a Coordinate
     * @param coord Coordinate of the wanted Tile. Upper left Tile has Coord = (0,0)
     * @return the TileUID corresponding to the Coordinate. If no Tile is found, an empty Optional is returned
     * @exception NoSuchElementException If no Coord is found, an exception is returned
     */
    //TODO check if this method is correct or necessary. Probably incorrect, maybe unnecessary
    public TileUID getPosition(Coord coord) {
        if(position.containsKey(coord))
            return position.get(coord);
        else
            throw new NoSuchElementException("This Coord does not exists");
    }


    /**
     * Returns the neighbors of the cell
     * @param tile the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical){
        return getTile(tile).getMapOfNeighbor(!logical);
    }

    /**
     * Returns all the pawns (player or domination points) contained in the tile
     * @param tile The tileID of the Tile requested
     * @return a Collection containing all the Damageable unit in the tile
     */
    public Collection<DamageableUID> containedPawns(TileUID tile){
        return getTile(tile).getDamageable();
    }

    /**
     * Returns the room containing the tile
     * @param tile tile requested
     * @return the RoomUID where the tile is
     */
    public RoomUID room(TileUID tile){
        return getTile(tile).getRoom();
    }

    /** Returns the tile where the pawn is
     * @param pawn pawn whose tile is requested
     * @return the room containing the pawn
     */
    public TileUID tile(DamageableUID pawn){
        for( TileUID t : tileUIDMap.keySet() ){
            if(getTile(t).getDamageable().contains(pawn))
                return t;
        }
        throw new NoSuchElementException("The pawn is not in the map");
    }

    /** Returns all the tiles inside a room
     *
     * @param room The room
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room){
        return getRoom(room).getTiles();
    }

}