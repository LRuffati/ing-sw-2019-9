package board;
import grabbables.Grabbable;
import uid.DamageableUID;
import uid.TileUID;
import uid.RoomUID;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The  building block of the map, keeps a reference of grabbable items inside of itself
 */
public class Tile{

    /**
     * Default constructor
     * @param map The reference to the board
     * @param tileID The identifier of the tile
     * @param roomID The identifier of the room
     * @param neighbors The neighbors of the tile
     */
    public Tile(GameMap map, RoomUID roomID, TileUID tileID, Map<Direction, NeighTile> neighbors, boolean spawnPoint) {
        this.map = map;
        this.roomID = roomID;
        this.tileID = tileID;
        this.neighbors = neighbors;
        this.spawnPoint = spawnPoint;

        damageable = new HashSet<>();
        grabbableSet = new HashSet<>();
    }

    /**
     * True if the tile is a Spawn Point, and can contain Weapons
     */
    private final boolean spawnPoint;

    /**
     * The UID of the room that contains the cell
     */
    private final RoomUID roomID;

    /**
     * If a cell is on the edge of the map it will not have an element mapped to the corresponding direction(s)
     *
     * Handle this via an appropriate getter
     */
    private final Map<Direction , NeighTile> neighbors;

    /**
     * The UID of the cell
     */
    private final TileUID tileID;

    /**
     * List of Grabbable elements (Weapon, TileCard)
     */
    private Set<Grabbable> grabbableSet;

    /**
     * List of Damageable units in the Tile
     */
    private Set<DamageableUID> damageable;

    /**
     * Reference of the global map
     */
    private transient GameMap map;


    protected void setMap(GameMap map){
        if (map==null)
            this.map = map;
        //Todo: review this silent fail
    }

    /**
     * Give the neightbor in a given direction, return an empty Optional if the tile has nothing on that direction
     * @param physical True for physical, False for logical
     * @param direction Direction of the asked tile
     * @return Optional is required in case the query is for a logical neighbor and there are none or for any query and the Tile is on the edge of the map
     */
    protected Optional<TileUID> getNeighbor(Boolean physical, Direction direction) {
        if(neighbors.containsKey(direction)) {
            return physical ? Optional.of(neighbors.get(direction).physical()) : neighbors.get(direction).logical();
        }
        else{
            return Optional.empty();
        }
    }

    /**
     * Get all cells in a given direction, including the current cell, until the neighbor of the last cell does not exist anymore
     * @param direction Direction of the asked tiles
     * @param physical True to pass through walls, False to only follow traversable links
     * @return The Collection is ordered from closest (always the current cell) to farthest
     */
    protected List<TileUID> getDirection(Boolean physical, Direction direction) {
        List<TileUID> ret = new ArrayList<>();
        ret.add(tileID);
        Optional<TileUID> t = getNeighbor(physical, direction);
        while(t.isPresent()) {
            ret.add(t.get());
            t = map.getTile(t.get()).getNeighbor(physical, direction);
        }
        return ret;
    }

    /** Returns a Set containing all the Grabbable elements in this Tile
     *
     * @return A Set containing all the Grabbable elements
     */
    protected Set<Grabbable> getGrabbable(){
        return new HashSet<>(grabbableSet);
    }

    /**
     * Adds a Grabbable elements in this Tile
     * @param grabbable The identifier of the grabbable item
     */
    protected void addGrabbable(Grabbable grabbable){
        grabbableSet.add(grabbable);
    }

    /**
     * Removes the element from the Grabbable set. If there is not this element, throws a NoSuchElementException
     * @param grabbable The identifier of the grabbable item
     * @throws NoSuchElementException If this GrabbableUID is not found, an exception is returned
     */
    protected void pickUpGrabbable(Grabbable grabbable) {
        if(grabbableSet.contains(grabbable))
            grabbableSet.remove(grabbable);
        else
            throw new NoSuchElementException("This GrabbableID is not in this Tile");
    }

    /**
     * Returns the color of the Room
     * @return Returns the color of the Room
     */
    public Color getColor(){
        return map.getRoom(roomID).getColor();
    }

    /**
     * Returns the RoomUID where the Tile is
     * @return The RoomUID that contains the Tile
     */
    protected RoomUID getRoom(){
        return roomID;
    }

    /**
     * Returns a Map containing surrounding Tiles. If there is no Tile, the corresponding direction is not present in the Map
     * @param physical true to check through walls, false otherwise
     * @return A Map containing surrounding Tiles
     */
    protected Map<Direction, TileUID> getMapOfNeighbor(boolean physical){
        Map<Direction, TileUID> ret = new EnumMap<>(Direction.class);
        Optional<TileUID> t;
        for( Direction d : neighbors.keySet()){
            t = getNeighbor(physical, d);

            t.ifPresent(x -> ret.put(d, x));
        }
        return ret;
    }

    /**
     * @return True iif the Tile is a spawnPoint
     */
    public boolean spawnPoint(){
        return this.spawnPoint;
    }
}
