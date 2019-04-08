package board;
import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;
import uid.RoomUID;
import uid.GrabbableUID;

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
    public Tile(GameMap map, RoomUID roomID, TileUID tileID, Map<Direction,NeightTile> neighbors) {
        this.map = map;
        this.roomID = roomID;
        this.tileID = tileID;
        this.neighbors = neighbors;
    }

    public Tile(){
        roomID = null;
        neighbors = null;
        tileID = null;
        map = null;
    }

    /**
     * The UID of the room that contains the cell
     */
    private final RoomUID roomID;

    /**
     * If a cell is on the edge of the map it will not have an element mapped to the corresponding direction(s)
     *
     * Handle this via an appropriate getter
     */
    private final Map<Direction , NeightTile> neighbors;

    /**
     * The UID of the cell
     */
    private final TileUID tileID;

    /**
     * List of Grabbable elements (Weapon, TileCard)
     */
    private List<GrabbableUID> grabbable;

    /**
     * List of Damageable units in the Tile
     */
    private List<DamageableUID> damageable;

    /**
     * Reference of the global map
     */
    private transient GameMap map;


    protected void setMap(GameMap map){
        this.map = map;
    }

    /**
     * This function gets all the cells within a certain distance of the cell, including the cell itself
     *
     * The result is a Set and an arbitrary range of (Logical in this case) distances [a,b] can be obtained by:
     *
     * getSurroundings(False, b).removeAll(getSurroundings(False, a) )
     * @param physical True for physical, False for logical
     * @param range The depth of the depth first search to run.
     * The function will return **all** cells at a distance less or equal this parameter
     *
     * If range is 0 the correct behaviour is to return this.tileID
     * @return The return value is a set instead of a Collection since it is not supposed to be ordered nor to have duplicates in it
     */
    protected Set<TileUID> getSurroundings(Boolean physical, Integer range) {
        Set<TileUID> ret = new HashSet<>(4*range+1);
        Set<TileUID> border = new HashSet<>();
        Set<TileUID> newBorder = new HashSet<>();
        ret.add(this.tileID);
        border.add(this.tileID);
        for(int i=0; i<range; i++) {
            //for(Integer i : range){
            for (TileUID t : border) {
                for (NeightTile t1 : map.getTile(t).neighbors.values()) {
                    TileUID t2;
                    t2 = physical ? t1.physical() : t1.logical().orElse(null);
                    /*if physical {
                        t2 = t1.physical().orElse(null);
                    }
                    else
                        t2 = t1.logical();*/

                    if (t2 != null) newBorder.add(t2);
                }
                ret.addAll(newBorder);
                border = new HashSet<>(newBorder);
                newBorder.clear();
            }
        }
        return ret;
    }

    /**
     * Give the neightbor in a given direction, return an empty Optional if the tile has nothing on that direction
     * @param physical True for physical, False for logical
     * @param direction Direction of the asked tile
     * @return Optional is required in case the query is for a logical neighbor and there are none or for any query and the Tile is on the edge of the map
     */
    protected Optional<TileUID> getNeighbor(Boolean physical, @NotNull Direction direction) {
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
            ret.add(tileID);
            t = map.getTile(t.get()).getNeighbor(physical, direction);
        }
        return ret;
    }

    /** Returns a List containing all the Grabbable elements in this Tile
     *
     * @return A List containing all the Grabbable elements
     */
    public List<GrabbableUID> getGrabbable(){
        return grabbable;
    }

    /**
     * Adds a Grabbable elements in this Tile
     * @param grabbableID The identifier of the grabbable item
     */
    protected void addGrabbable(GrabbableUID grabbableID){
        this.grabbable.add(grabbableID);
    }

    /**
     * Removes the element from the Grabbable List. If there is not this element, throws a NoSuchElementException
     * @param grabbableID The identifier of the grabbable item
     * @throws NoSuchElementException If this GrabbableUID is not found, an exception is returned
     */
    protected void pickUpGrabbable(GrabbableUID grabbableID) {
        if(grabbable.contains(grabbableID))
            grabbable.remove(grabbableID);
        else
            throw new NoSuchElementException("This GrabbableID is not in this Tile");
    }

    /**
     * Returns the color of the Room
     * @return Returns the color of the Room
     */
    protected Color getColor(){
        return map.getRoom(roomID).getColor();
    }

    /**
     * Returns the RoomUID where the Tile is
     * @return The RoomUID that contains the Tile
     */
    protected RoomUID getRoom(){
        return roomID;
    }


    private Room getRoomObj(){
        return map.getRoom(roomID);
    }

    /**
     * Finds all the Tiles that are visible from itself
     * @return A Set containing all the visible Tiles
     */
    protected Set<TileUID> getVisible(){
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> surr = getSurroundings(true, 1);
        for(TileUID t : surr){
            Iterator<TileUID> iterator = map.getTile(t).getRoomObj().getTilesIterator();
            while(iterator.hasNext()){
                ret.add(iterator.next());
            }
        }
        return ret;
    }


    /**
     * Returns a collection with all the DamageableUID contained in the tile
     * @return A collections containing the DamageableUID in the tile
     */
    public Collection<DamageableUID> getDamageable(){
        return damageable;
    }

    /**
     * Adds a Damageable element in the tile
     * @param damageableUID The Damageable element that has to be added
     */
    public void addDamageable(DamageableUID damageableUID){
        damageable.add(damageableUID);
    }

    /**
     * Returns a Map containing surrounding Tiles. If there is no Tile, the corresponding direction is not present in the Map
     * @param physical true to check through walls, false otherwise
     * @return A Map containing surrounding Tiles
     */
    protected Map<Direction, TileUID> getMapOfNeighbor(Boolean physical){
        Map<Direction, TileUID> ret = new EnumMap<>(Direction.class);
        Optional<TileUID> t;
        for( Direction d : neighbors.keySet()){
            t = getNeighbor(physical, d);
            if(t.isPresent())
                ret.put(d, t.get());
        }
        return ret;
    }

}
