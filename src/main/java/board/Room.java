package board;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.awt.Color.*;

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

    private String ansiColor;

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
     * @return The identifier of the Room
     */
    public RoomUID getRoomID(){
        return this.roomID;
    }

    /**
     * @return The color used to identify the room
     */
    public Color getColor(){
        return color;
    }

    /**
     * @return A collection containing all the Tiles in the room
     */
    public Collection<TileUID> getTiles(){
        return new HashSet<>(tiles);
    }

    public String getAnsi(){
        if(color == white) return "\u001B[37m";
        if(color == black) return "\u001B[30m";
        if(color == red) return "\u001B[31m";
        if(color == green) return "\u001B[32m";
        if(color == yellow) return "\u001B[33m";
        if(color == blue) return "\u001B[34m";
        return "\u001B[0m";
    }
}