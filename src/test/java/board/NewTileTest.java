package board;

import genericitems.Tuple3;
import grabbables.AmmoCard;
import grabbables.Grabbable;
import grabbables.PowerUp;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.TileUID;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.*;

class NewTileTest {

    private GameMap map;

    @BeforeEach
    void setup(){
        map = null;
        try {
            map = GameMap.gameMapFactory("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt"
                    ,0, new Tuple3<>(null,null,null));
            //map = ParserMap.parseMap("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt");
        }
        catch (FileNotFoundException e){
        }
    }

    @Test
    void getNeighborTest(){
        assertEquals(Optional.empty(), map.getTile(map.getPosition(new Coord(0,0))).getNeighbor(true, Direction.UP));
        assertEquals(Optional.empty(), map.getTile(map.getPosition(new Coord(0,0))).getNeighbor(false, Direction.UP));

        assertEquals(Optional.of(map.getPosition(new Coord(2,2))), map.getTile(map.getPosition(new Coord(1,2))).getNeighbor(true, Direction.DOWN));
        assertEquals(Optional.empty(), map.getTile(map.getPosition(new Coord(1,2))).getNeighbor(false, Direction.DOWN));
    }

    @Test
    void getDirectionTest(){
        List<TileUID> res = new ArrayList<>();
        res.add(map.getPosition(new Coord(0,2)));
        res.add(map.getPosition(new Coord(1,2)));
        res.add(map.getPosition(new Coord(2,2)));
        assertEquals(res , map.getTile(map.getPosition(new Coord(0,2))).getDirection(true, Direction.DOWN));

        res.clear();
        res.add(map.getPosition(new Coord(0,2)));
        res.add(map.getPosition(new Coord(1,2)));
        assertEquals(res , map.getTile(map.getPosition(new Coord(0,2))).getDirection(false, Direction.DOWN));

        res.clear();
        res.add(map.getPosition(new Coord(0,2)));
        assertEquals(res , map.getTile(map.getPosition(new Coord(0,2))).getDirection(false, Direction.UP));
    }

    @Test
    void getMapOfNeighbor(){
        Tile tile = map.getTile(map.getPosition(new Coord(1,1)));
        Map<Direction, TileUID> m;
        m = tile.getMapOfNeighbor(true);
        for(Direction d : m.keySet()){
            assertEquals(tile.getNeighbor(true, d).get() , m.get(d));
        }
        m.clear();
        m = tile.getMapOfNeighbor(false);
        for(Direction d : m.keySet()){
            assertEquals(tile.getNeighbor(false, d).get() , m.get(d));
        }

        assertNull(m.get(Direction.UP));
    }

    @Test
    void testColor(){
        Tile tile = map.getTile(map.getPosition(new Coord(1,1)));
        //testColor
        assertEquals(map.getRoom(tile.getRoom()).getColor(), tile.getColor());
    }

    @Test
    void testSpawn(){
        assertFalse(map.getTile(map.getPosition(new Coord(0,0))).spawnPoint());
        assertFalse(map.getTile(map.getPosition(new Coord(0,3))).spawnPoint());
        assertTrue(map.getTile(map.getPosition(new Coord(0,2))).spawnPoint());
    }


    @Test
    void testGrabbable(){
        Tile tile = map.getTile(map.getPosition(new Coord(1,1)));
        //TODO: check

        Set<Grabbable> g;
        Grabbable g1 = new PowerUp(null, null);
        g = tile.getGrabbable();
        assertTrue(g.isEmpty());
        g.add(g1);
        assertNotEquals(g, tile.getGrabbable());
        assertThrows(NoSuchElementException.class , () -> tile.pickUpGrabbable(g1));

        tile.addGrabbable(g1);
        g = tile.getGrabbable();
        assertFalse(g.isEmpty());
        assertEquals(g, tile.getGrabbable());
        tile.pickUpGrabbable(g1);
        assertTrue(tile.getGrabbable().isEmpty());
        assertEquals(new HashSet<>() , tile.getGrabbable());
    }

    @Test
    void testDamageable(){
        Tile tile = map.getTile(map.getPosition(new Coord(1,1)));
        //TODO: check
        Set<DamageableUID> g;
        DamageableUID g1 = new DamageableUID();
        g = (HashSet) tile.getDamageable();
        assertTrue(g.isEmpty());
        g.add(g1);
        assertNotEquals(g, tile.getDamageable());
        assertThrows(NoSuchElementException.class , () -> tile.removeDamageable(g1));

        tile.addDamageable(g1);
        g = (HashSet) tile.getDamageable();
        assertFalse(g.isEmpty());
        assertEquals(g, tile.getDamageable());
        tile.removeDamageable(g1);
        assertTrue(tile.getDamageable().isEmpty());
        assertEquals(new HashSet<>() , tile.getDamageable());
    }

}
