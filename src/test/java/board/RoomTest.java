package board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    HashSet<TileUID> tiles = new HashSet<>();
    Color color;
    @BeforeEach
    void setUp() {
        tiles.add(new TileUID());
        tiles.add(new TileUID());
        tiles.add(new TileUID());
        tiles.add(new TileUID());
        tiles.add(new TileUID());

        color = new Color(10,10,10);
    }

    void initColor(){

    }

    @Test
    void testColor(){
        Room room = new Room(new RoomUID(), tiles, color);
        assertEquals(new Color(10,10,10), room.getColor());
    }

    @Test
    void testGetTilesNotEmpty(){
        Room room = new Room(new RoomUID(), tiles, color);
        Iterator<TileUID> i = room.getTiles();
        while(i.hasNext()) {
            TileUID tile = i.next();
            assertTrue(tiles.contains(tile));
        }
    }

    @Test
    void testGetTilesEmpty(){
        Room room = new Room(new RoomUID(), new HashSet<>(), color);
        Iterator<TileUID> i = room.getTiles();
        while(i.hasNext()) {
            assertTrue(tiles.contains(i.next()));
        }
    }

    @Test
    void removeItemFromIterator(){
        Room room = new Room(new RoomUID(), tiles, color);
        Iterator<TileUID> i = room.getTiles();
        assertThrows(UnsupportedOperationException.class , i::remove);
        int c = 0;
        while(i.hasNext()) {
            i.next();
            c++;
        }
        assertEquals(tiles.size(), c);
    }
}
