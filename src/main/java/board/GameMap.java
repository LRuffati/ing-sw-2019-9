package board;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

import actions.targeters.targets.BasicTarget;
import actions.targeters.targets.RoomTarget;
import actions.targeters.targets.TileTarget;
import genericitems.Tuple4;
import grabbables.Deck;
import grabbables.Grabbable;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;
import uid.RoomUID;
/**
 * The logical container of all elements of the map, room, tile, pawns, munition cards and grabbable weapons cards
 */
public class GameMap {
    //TODO Damageable statico, non modificabili

    /**
     * Private constructor, to build a map gameMapFactory must be used
     */
    GameMap(Map<RoomUID, Room> roomUIDMap,
                   Map<TileUID, Tile> tileUIDMap,
                   List<TileUID> position,
                   Coord maxPos,
                   int numOfPlayer) {
        this.roomUIDMap = roomUIDMap;
        this.tileUIDMap = tileUIDMap;
        this.position = position;
        this.maxPos = maxPos;

        this.emptyTile = new TileUID();

        for(Tile t : tileUIDMap.values()){
            t.setMap(this);
        }

        this.damageableUIDMap = buildPawn(this, numOfPlayer);

    }


    public static GameMap gameMapFactory(String path, int numOfPlayer) throws FileNotFoundException {
        Tuple4 res = ParserMap.parseMap(path);
        return new GameMap(
                    (Map<RoomUID, Room>)res.x,
                    (Map<TileUID, Tile>)res.y,
                    (List<TileUID>)res.z,
                    (Coord)res.t, numOfPlayer);
    }


    public Sandbox createSandbox() {
        Map<RoomUID, RoomTarget> targetRooms = roomUIDMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new RoomTarget(e.getKey())));

        Map<TileUID, TileTarget> targetTiles = tileUIDMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new TileTarget(e.getKey())));

        Map<DamageableUID, BasicTarget> targetPawns = damageableUIDMap.entrySet()
                .stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> BasicTarget.basicFactory(e.getValue())));

        return new Sandbox(targetRooms, targetTiles, targetPawns, this);
    }

    /**
     * Holds the length and width of the Map
     */
    private Coord maxPos;

    /**
     * Map between RoomUID and the Room Class
     */
    private Map<RoomUID, Room> roomUIDMap;

    /**
     * Map between TileUID and the Tile Class
     */
    private Map<TileUID, Tile> tileUIDMap;

    /**
     * Stores the absolute position of each Tile. Should not be used to access to TileUID
     */
    private List<TileUID> position;

    /**
     * Map between DamageableUID and Damageable Class
     */
    private Map<DamageableUID, Pawn> damageableUIDMap;


    private TileUID emptyTile;


    private Deck<Grabbable> deckOfWeapon;
    private Deck<Grabbable> deckOfAmmoCard;
    private Deck<Grabbable> deckOfPowerUp;


    /**
     * Returns the Tile Object given a TileUID
     *
     * @param tileID TileUID of the wanted Tile
     * @return the Tile corresponding to the TileUID
     * @throws NoSuchElementException If no Tile is found, an exception is returned
     */
    public Tile getTile(TileUID tileID) {
        if (tileUIDMap.containsKey(tileID))
            return tileUIDMap.get(tileID);
        else
            throw new NoSuchElementException("This TileUID does not exists");
    }

    /**
     * Returns the Room Object given a RoomUID
     *
     * @param roomID RoomUID of the wanted Room
     * @return the Room corresponding to the RoomUID
     * @throws NoSuchElementException If no Room is found, an exception is returned
     */
    public Room getRoom(RoomUID roomID) {
        if (roomUIDMap.containsKey(roomID))
            return roomUIDMap.get(roomID);
        else
            throw new NoSuchElementException("This RoomUID does not exists");
    }

    /**
     * Returns the Damageable Object given a DamageableUID
     *
     * @param damageableUID DamageableUID of the wanted Damageable
     * @return the Damageable corresponding to the DamageableUID
     * @throws NoSuchElementException If no Damageable is found, an exception is returned
     */
    public Pawn getPawn(DamageableUID damageableUID) {
        if (damageableUIDMap.containsKey(damageableUID))
            return damageableUIDMap.get(damageableUID);
        else
            throw new NoSuchElementException("This DamageableUID does not exists");
    }

    /**
     * Returns the TileUID Object given a Coordinate
     *
     * @param coord Coordinate of the wanted Tile. Upper left Tile has Coord = (0,0)
     * @return the TileUID corresponding to the Coordinate. If no Tile is found, an empty Optional is returned
     * @throws NoSuchElementException If no Coord is found, an exception is returned
     */
    public TileUID getPosition(Coord coord) {
        int pos = coord.getX() * maxPos.getX() + coord.getY();
        if (pos < maxPos.getX() * maxPos.getY()
                && position.get(pos) != null
                && coord.getX() <= maxPos.getX()
                && coord.getY() <= maxPos.getY())
            return position.get(pos);
        else
            throw new NoSuchElementException("This Coord does not exists");
    }


    /**
     *
     * @return A Set containing all the Tiles in the map
     */
    public Set<TileUID> allTiles(){
        return new HashSet<>(tileUIDMap.keySet());
    }


    /**
     * Returns the neighbors of the cell
     *
     * @param tile    the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical) {
        return getTile(tile).getMapOfNeighbor(!logical);
    }

    /**
     * Returns all the pawns (player or domination points) contained in the tile
     *
     * @param tile The tileID of the Tile requested
     * @return a Collection containing all the Damageable unit in the tile
     */
    public Collection<DamageableUID> containedPawns(TileUID tile) {
        return getTile(tile).getDamageable();
    }

    /**
     * Returns the room containing the tile
     *
     * @param tile tile requested
     * @return the RoomUID where the tile is
     */
    public RoomUID room(TileUID tile) {
        return getTile(tile).getRoom();
    }

    /**
     * Returns the tile where the pawn is
     *
     * @param pawn pawn whose tile is requested
     * @return the room containing the pawn
     */
    public TileUID tile(DamageableUID pawn) {
        for (TileUID t : tileUIDMap.keySet()) {
            if (getTile(t).getDamageable().contains(pawn))
                return t;
        }
        throw new NoSuchElementException("The pawn is not in the map");
    }

    /**
     * Returns all the tiles inside a room
     *
     * @param room The room
     * @return All the tiles in the room
     */
    public Collection<TileUID> tilesInRoom(RoomUID room) {
        return getRoom(room).getTiles();
    }

    /**
     * This function gets all the cells within a certain distance of the cell, including the cell itself
     * <p>
     * The result is a Set and an arbitrary range of (Logical in this case) distances [a,b] can be obtained by:
     * <p>
     * getSurroundings(False, b).removeAll(getSurroundings(False, a) )
     *
     * @param physical True for physical, False for logical
     * @param range    The depth of the depth first search to run.
     *                 The function will return **all** cells at a distance less or equal this parameter
     *                 <p>
     *                 If range is 0 the correct behaviour is to return this.tileID
     *                 If less than 0 return an empty set
     * @param tile
     * @return The return value is a set instead of a Collection since it is not supposed to be ordered nor to have duplicates in it
     */
    public Set<TileUID> getSurroundings(Boolean physical, Integer range, TileUID tile) {
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> border = new HashSet<>();
        Set<TileUID> newBorder = new HashSet<>();
        if (range < 0) return ret;
        ret.add(tile);
        border.add(tile);
        for (int i = 0; i < range; i++) {
            for (TileUID t : border) {
                newBorder.addAll(neighbors(t, !physical).values());
                ret.addAll(newBorder);
            }
            border = new HashSet<>(newBorder);
            newBorder.clear();
        }
        return ret;
    }

    /**
     * Finds all the Tiles that are visible from itself
     *
     * @param tile
     * @return A Collection containing all the visible Tiles
     */
    public Collection<TileUID> getVisible(TileUID tile) {
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> surr = getSurroundings(false, 1, tile);
        for (TileUID t : surr) {
            ret.addAll(tilesInRoom(room(t)));
        }
        return ret;
    }


    /**
     * Returns a Set containing all the Grabbable elements in this Tile
     *
     * @return A Set containing all the Grabbable elements
     */
    public Set<Grabbable> getGrabbable(TileUID tile) {
        return new HashSet<>(getTile(tile).getGrabbable());
    }

    /**
     * Adds a Grabbable elements to the Tile, and checks if the Card is correct
     *
     * @param tile      the tile where the card must be put
     * @param grabbable the card
     */
    public void addGrabbable(TileUID tile, Grabbable grabbable) {
        if (getTile(tile).spawnPoint() && deckOfWeapon.isPicked(grabbable))
            getTile(tile).addGrabbable(grabbable);
        else if (!getTile(tile).spawnPoint() && deckOfAmmoCard.isPicked(grabbable))
            getTile(tile).addGrabbable(grabbable);
        else
            throw new InvalidParameterException("The card cannot be added");
    }

    /**
     * Removes the element from the Grabbable set. If there is not this element, throws a NoSuchElementException
     *
     * @param grabbable The identifier of the grabbable item
     * @throws NoSuchElementException If this GrabbableUID is not found, an exception is returned
     */
    public void pickUpGrabbable(TileUID tile, Grabbable grabbable) {
        getTile(tile).pickUpGrabbable(grabbable);
        //TODO: da fare adesso o alla fine del turno?
        if (getTile(tile).spawnPoint()) {
            addGrabbable(tile, deckOfWeapon.next());
        } else {
            addGrabbable(tile, deckOfAmmoCard.next());
        }
    }

    /**
     * Returns a collection with all the DamageableUID contained in the tile
     *
     * @return A collections containing the DamageableUID in the tile
     */
    protected Collection<DamageableUID> getDamageable(TileUID tile) {
        return getTile(tile).getDamageable();
    }

    /**
     * Adds a Damageable element in the tile
     *
     * @param damageableUID The Damageable element that has to be added
     */
    protected void addDamageable(TileUID tile, DamageableUID damageableUID) {
        getTile(tile).addDamageable(damageableUID);
    }

    /**
     * Removes the element from the Damageable Set. If there is not this element, throws a NoSuchElementException
     *
     * @param damageableID The identifier of the damageable item
     * @throws NoSuchElementException If this DamageableUID is not found, an exception is returned
     */
    protected void removeDamageable(TileUID tile, DamageableUID damageableID) {
        getTile(tile).removeDamageable(damageableID);
    }

    protected Set<DamageableUID> getDamageable(){
        return damageableUIDMap.keySet();
    }



    private Map<DamageableUID, Pawn> buildPawn(GameMap map, int numOfPlayer){
        Map<DamageableUID, Pawn> res = new HashMap<>();
        Pawn p;
        DamageableUID uid;
        for(int i=0; i<numOfPlayer; i++){
            uid = new DamageableUID();
            p = new Pawn(uid, map.emptyTile);
            res.put(uid, p);
        }
        return res;
    }
}