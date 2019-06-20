package board;

import gamemanager.ParserConfiguration;
import genericitems.Tuple3;
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
            map =  GameMap.gameMapFactory(ParserConfiguration.parsePath("map1Path")
                    , 0, new Tuple3<>(null,null,null));
        }
        catch (FileNotFoundException e){
            System.exit(-100);
        }

        assertNotNull(map);
        assertEquals(map.getPosition(new Coord(0,0)), map.getTile(map.getPosition(new Coord(0,1))).getNeighbor(false, Direction.LEFT).get());

        Set<TileUID> res = new HashSet<>();
        res.add(map.getPosition(new Coord(2,2)));

        res.add(map.getPosition(new Coord(1,2)));
        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,3)));

        res.add(map.getPosition(new Coord(1,1)));
        res.add(map.getPosition(new Coord(0,2)));
        res.add(map.getPosition(new Coord(1,3)));

        assertEquals(res, map.getSurroundings(true, 2, map.getPosition(new Coord(2,2))));

        res.clear();
        res.add(map.getPosition(new Coord(2,2)));

        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,3)));

        res.add(map.getPosition(new Coord(1,1)));
        res.add(map.getPosition(new Coord(1,3)));
        assertEquals(res, map.getSurroundings(false, 2, map.getPosition(new Coord(2,2))));
    }

    @Test
    void invalidPathTest(){
        assertThrows(NullPointerException.class , () -> ParserMap.parseMap("invalidPath"));
    }
}
