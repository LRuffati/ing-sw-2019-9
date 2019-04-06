package board;
import uid.TileUID;
import uid.RoomUID;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The  building block of the map, keeps a reference of grabbable items inside of itself
 */
public class Tile{

    /**
     * Default constructor
     */
    public Tile() {
        // TODO
    }

    /**
     * The UID of the room that contains the cell
     */
    private RoomUID roomID;

    /**
     * If a cell is on the edge of the map it will not have an element mapped to the corresponding direction(s)
     *
     * Handle this via an appropriate getter
     */
    private Map<Direction , NeightTile> neighbors;

    /**
     * List of Grabbable elements (Weapon, TileCard)
     */
    //private List<Grabbable> grabbable;

    /**
     * The UID of the cell
     */
    private TileUID tileID;

    /**
     * Reference of the global map
     */
    private transient GameMap map;




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
                for (NeightTile t1 : map.getTile(t).get().neighbors.values()) {
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
    public Optional<TileUID> getNeighbor(Boolean physical, Direction direction) {
        return neighbors.containsKey(direction) ? (physical ? Optional.of(neighbors.get(direction).physical()) : neighbors.get(direction).logical()) : Optional.empty();
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
            t = map.getTile(t.get()).get().getNeighbor(physical, direction);
        }
        return ret;
    }

    /*

    protected List<Grabbable> getGrabbable(){
        return grabbable;
        //TODO
    }

    protected void addGrabbable(Grabbable grabbable){
        this.grabbable.add(grabbable);
        //TODO
    }

    protected Grabbable pickUpGrabbable(Grabbable grabbable){
        //TODO
    }

    protected Color getColor(){
        return map.getRoom(this.roomID).get().getColor();
    }

    protected Color getColor(){
        Optional<Room> r = map.getRoom(roomID);
        return r.isPresent() ? r.get().getColor() : null;
        map.getRoom(roomID).ifPresent(Optional.);

    }


    protected Room getRoom(){
        return map.getRoom(roomID).isPresent().get();
    }

    protected Set<TileUID> getVisible(){
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> surr = getSurroundings(true, 1);
        for(TileUID t : surr){
            map.getTile(t).ifPresent(x -> x.getRoom().getTiles().addAll(ret));
            //map.getTile(t).stream().filter(x->x.ofNullable()).forEach(getRoom().getTiles().addAll(ret));
        }
        return ret;
    }
    */
}
