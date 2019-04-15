package board;

import org.junit.jupiter.api.*;
import uid.TileUID;

import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class ParserMapTest {
    @Test
    void map1Test(){
        GameMap map = null;
        try {
            map = ParserMap.parseMap("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt");
        }
        catch (FileNotFoundException e){
            System.exit(-100);
        }

        assertEquals(map.getPosition(new Coord(0,0)), map.getTile(map.getPosition(new Coord(0,1))).getNeighbor(false, Direction.LEFT).get());

        Set<TileUID> res = new HashSet<>();
        res.add(map.getPosition(new Coord(2,2)));

        res.add(map.getPosition(new Coord(1,2)));
        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,3)));

        res.add(map.getPosition(new Coord(1,1)));
        res.add(map.getPosition(new Coord(0,2)));
        res.add(map.getPosition(new Coord(1,3)));

        assertEquals(res, map.getTile(map.getPosition(new Coord(2,2))).getSurroundings(true, 2));

        res.clear();
        res.add(map.getPosition(new Coord(2,2)));

        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,3)));

        res.add(map.getPosition(new Coord(1,1)));
        res.add(map.getPosition(new Coord(1,3)));
        assertEquals(res, map.getTile(map.getPosition(new Coord(2,2))).getSurroundings(false, 2));
    }

    @Test
    void invalidPathTest(){
        assertThrows(FileNotFoundException.class , () -> ParserMap.parseMap("invalidPath"));
    }
}
