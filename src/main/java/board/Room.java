package board;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.*;

/**
 * The logical representation of a room in the map.
 * When visualized all cells in the room should have the same color, unique for each room.
 * All cells in the same room see each other
 */
public class Room {

    /**
     * The UID of the Room.
     */
    private RoomUID roomID;

    /**
     * The Color used for the Room. All rooms have different colors.
     */
    private Color color;

    /**
     * All the TileUID in the same Room.
     */
    private HashSet<TileUID> tiles;

    /**
     * Construct the room.
     * @param tiles The tiles that are contained in the room
     * @param color The color of the room
     */
    public Room(HashSet<TileUID> tiles, Color color) {
        this.tiles = tiles;
        this.color = color;
    }

    /**
     * @return The color used to identify the room
     */
    protected Color getColor(){
        return color;
    }

    /**
     * @return A Set containing all the TileUID in the room
     */
    protected HashSet<TileUID> getTiles(){
        return tiles;
    }

}