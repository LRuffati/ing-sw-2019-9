package board;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.Iterator;
import java.util.Set;

/**
 * The logical representation of a room in the map.
 * When visualized all cells in the room should have the same color, unique for each room.
 * All cells in the same room see each other
 */
public class Room{

    /**
     * The UID of the Room.
     */
    private final RoomUID roomID;

    /**
     * The Color used for the Room. All rooms have different colors.
     */
    private final Color color;

    /**
     * All the TileUID in the same Room.
     */
    private final Set<TileUID> tiles;

    /**
     * Construct the room.
     * @param roomID The UID of the room
     * @param tiles The tiles that are contained in the room
     * @param color The color of the room
     */
    public Room(RoomUID roomID, Set<TileUID> tiles, Color color) {
        this.tiles = tiles;
        this.color = color;
        this.roomID = roomID;
    }

    /**
     * @return The color used to identify the room
     */
    protected Color getColor(){
        return color;
    }

    /**
     * @return An iterator that allow to access to the Tiles in the room
     */
 /*   protected Set<TileUID> getTiles(){
        return tiles;
    }

    public Iterator<TileUID> iterator(){
        return new Iterator<TileUID>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                //return index == tiles.size();
                return tiles.iterator().hasNext();
            }

            @Override
            public TileUID next() {
                return tiles.iterator().next();
            }
        };
    }*/
    protected Iterator<TileUID> getTiles(){
        return tiles.iterator();
    }

}