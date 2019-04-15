package board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class newGameMapTest {
    private GameMap map;

    @BeforeEach
    void setup(){
        map = null;
        try {
            map = ParserMap.parseMap("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt");
        }
        catch (FileNotFoundException e){
        }
    }

    @Test
    void neighborTest(){
        helperTest(map.getPosition(new Coord(0,0)),true);
        helperTest(map.getPosition(new Coord(0,0)),false);
        helperTest(map.getPosition(new Coord(1,1)),true);
        assertTrue(map.neighbors(map.getPosition(new Coord(0,3)),false).isEmpty());
    }
    private void helperTest(TileUID t, boolean logical){
        Map<Direction, TileUID> m;
        Map<Direction, TileUID> m1;
        m = map.neighbors(t,logical);
        m1 = map.getTile(t).getMapOfNeighbor(!logical);
        for(Direction d : m.keySet()){
            for(Direction d1 : m.keySet()) {
                if(d.equals(d1))
                    assertEquals(m.get(d) , m1.get(d1));
            }
        }
    }

    @Test
    void pawnTest(){
        //TODO: add pawn
        /*
        assertThrows(NoSuchElementException.class , () -> map.tile(pawn1));
        map.getTile(thisTile).addDamageable(pawn1);
        assertEquals(thisTile ,  map.tile(pawn1));
        assertEquals(Set.of(pawn1) , map.containedPawns(thisTile));
        tile.removeDamageable(pawn1);
        assertThrows(NoSuchElementException.class , () -> map.tile(pawn1));
        assertTrue(map.containedPawns(thisTile).isEmpty());
        assertTrue(map.containedPawns(tLeft).isEmpty());
        */
    }

    @Test
    void sandboxTest(){
        //TODO: add Pawn
        /*
        Sandbox s = null;
        s = map.createSandbox();
        assertNotNull(s);
        */
    }

    @Test
    void containedPawnsTest(){
        //TODO: add Paw
    }

    @Test
    void tileTest(){
        //TODO: add Pawn
    }


    @Test
    void getterTest(){
        assertThrows(NoSuchElementException.class , () -> map.getTile(new TileUID()));

        assertThrows(NoSuchElementException.class , () -> map.getRoom(new RoomUID()));

        assertThrows(NoSuchElementException.class , () -> map.getPosition(new Coord(0,5)));

        TileUID t = map.getPosition(new Coord(0,0));
        assertTrue(map.getRoom(map.room(t)).getTiles().contains(t));
    }

    @Test
    void neighborTest2(){
        assertEquals(map.getTile(map.getPosition(new Coord(2,3))).getMapOfNeighbor(true) ,
                map.neighbors(map.getPosition(new Coord(2,3)), true));
        assertEquals(map.getTile(map.getPosition(new Coord(2,3))).getMapOfNeighbor(false) ,
                map.neighbors(map.getPosition(new Coord(2,3)), true));
        assertEquals(map.getTile(map.getPosition(new Coord(0,3))).getMapOfNeighbor(true) ,
                map.neighbors(map.getPosition(new Coord(0,3)), true));

        assertTrue(map.neighbors(map.getPosition(new Coord(0,3)), true).isEmpty());

        assertEquals(2, map.neighbors(map.getPosition(new Coord(2,3)), true).size());
    }


    @Test
    void tilesInRoomTest(){
        HashSet<TileUID> res = new HashSet<>();
        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,2)));
        res.add(map.getPosition(new Coord(2,2)));
        assertEquals(res , map.tilesInRoom(map.room(map.getPosition(new Coord(2,2)))));
        assertEquals(res , map.tilesInRoom(map.getTile(map.getPosition(new Coord(2,2))).getRoom()));
    }

    @Test
    void getSurroundingsTest(){
        Collection<TileUID> res;

        res = map.getSurroundings(false, 3, map.getPosition(new Coord(0,0)));
        assertEquals(7, res.size());

        res = map.getSurroundings(false, 5, map.getPosition(new Coord(0,0)));
        assertEquals(10, res.size());

        res = map.getSurroundings(true, 2, map.getPosition(new Coord(2,2)));
        assertEquals(7, res.size());

        res = map.getSurroundings(true, 0, map.getPosition(new Coord(2,2)));
        assertEquals(1, res.size());
    }

    @Test
    void getVisibleTest(){
        ArrayList<TileUID> res = new ArrayList<>();
        res.add(map.getPosition(new Coord(0,0)));
        res.add(map.getPosition(new Coord(0,1)));
        res.add(map.getPosition(new Coord(0,2)));
        assertTrue(map.getVisible(map.getPosition(new Coord(0,1))).containsAll(res));
        assertTrue(map.getVisible(map.getPosition(new Coord(0,1)))
                .containsAll(map.tilesInRoom(map.room(map.getPosition(new Coord(0,0))))));

        res.clear();
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(0,0)))));
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(1,0)))));
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(1,3)))));
        assertTrue(map.getVisible(map.getPosition(new Coord(1,2))).containsAll(res));
    }

    /*
    @Test
    void testGetters(){
        assertEquals(map.getTile(thisTile) , tile);
        assertEquals(map.getTile(tUp) , tileUp);
        assertThrows(NoSuchElementException.class , () -> map.getTile(new TileUID()));

        assertEquals(roomObj , map.getRoom(room));
        assertThrows(NoSuchElementException.class , () -> map.getRoom(new RoomUID()));

        assertEquals(p1 , map.getDamageable(pawn1));
        assertEquals(p2 , map.getDamageable(pawn2));
        assertThrows(NoSuchElementException.class , () -> map.getDamageable(new DamageableUID()));

        assertEquals(room , map.room(thisTile));
        assertEquals(room , map.room(tUp));

        assertEquals(Set.of(thisTile,tUp,tDown,tRight) , map.tilesInRoom(room));
    }
    */


}
