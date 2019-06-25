package board;

import controller.GameMode;
import gamemanager.ParserConfiguration;
import genericitems.Tuple3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.RoomUID;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private GameMap map;

    @BeforeEach
    void setup() {
        map = null;
        map = GameMap.gameMapFactory(GameMode.NORMAL, ParserConfiguration.parsePath("map1Path")
                , 0, new Tuple3<>(null, null, null));
        //map = ParserMap.parseMap("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt");
    }

    @Test
    void getColorTest(){
        assertEquals(Color.BLUE , map.getRoom(map.room(map.getPosition(new Coord(0,0)))).getColor());
        assertEquals(Color.WHITE , map.getRoom(map.room(map.getPosition(new Coord(2,1)))).getColor());
        assertEquals(Color.YELLOW , map.getRoom(map.room(map.getPosition(new Coord(2,3)))).getColor());
    }

    @Test
    void getTilesTest(){
        Room r;
        r = map.getRoom(map.room(map.getPosition(new Coord(0,0))));
        assertTrue(r.getTiles().contains(map.getPosition(new Coord(0,0))));
        r = map.getRoom(map.room(map.getPosition(new Coord(0,0))));
        assertTrue(r.getTiles().contains(map.getPosition(new Coord(0,2))));
        r = map.getRoom(map.room(map.getPosition(new Coord(2,2))));
        assertFalse(r.getTiles().contains(map.getPosition(new Coord(2,3))));

        assertThrows(NoSuchElementException.class , () -> map.getRoom(new RoomUID()).getTiles());

        Room room = new Room(new RoomUID(), new HashSet<>(), null);
        assertTrue(room.getTiles().isEmpty());
    }
}
