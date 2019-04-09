package board;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
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
    public Color getColor(){
        return color;
    }

    /**
     * @return An iterator that allow to access to the Tiles in the room. Remove() launch an UnsupportedOperationException is called
     */
    public Iterator<TileUID> getTilesIterator(){
        Iterator<TileUID> iterator = tiles.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public TileUID next() {
                return iterator.next();
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException("Remove method can't be used here");
            }
        };
    }

    /**
     * @return A collection containing all the Tiles in the room
     */
    public Collection<TileUID> getTiles(){
        return new HashSet<>(tiles);
    }
}