package board;

import org.junit.jupiter.api.*;
import player.Actor;
import player.Pawn;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class SandBoxTest {
    private GameMap map;
    private Sandbox sandbox;

    @BeforeEach
    void setup(){
        map = null;
        try {
            map = GameMap.gameMapFactory("C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt"
                    , 3);
        }
        catch (FileNotFoundException e){
        }
        sandbox = map.createSandbox();
    }

    @Test
    void testNeighbors(){
        helperNeighbor(map.getPosition(new Coord(0,0)),true);
        helperNeighbor(map.getPosition(new Coord(0,0)),false);
        helperNeighbor(map.getPosition(new Coord(0,2)),true);
    }
    private void helperNeighbor(TileUID t, boolean logical){
        Map<Direction, TileUID> m;
        Map<Direction, TileUID> m1;
        m = sandbox.neighbors(t,logical);
        m1 = map.getTile(t).getMapOfNeighbor(!logical);
        for(Direction d : m.keySet()){
            for(Direction d1 : m.keySet()) {
                if(d.equals(d1))
                    assertEquals(m.get(d) , m1.get(d1));
            }
        }
    }

    @Test
    void testCircle(){
        helperCircle(map.getPosition(new Coord(0,0)),10,true);
        helperCircle(map.getPosition(new Coord(0,0)),10,false);
        helperCircle(map.getPosition(new Coord(0,0)),0,true);
        helperCircle(map.getPosition(new Coord(0,0)),0,false);
    }
    private void helperCircle(TileUID t, int radius, boolean logical){
        assertEquals(map.getSurroundings(!logical, radius, t) , sandbox.circle(t,radius, logical));
    }

    @Test
    void testVarious(){
        assertTrue(sandbox.allTiles().containsAll(map.allTiles()));

        assertTrue(sandbox.tilesSeen(map.getPosition(new Coord(0,0))).containsAll(map.getVisible(map.getPosition(new Coord(0,0)))));
        assertTrue(sandbox.tilesSeen(map.getPosition(new Coord(2,1))).containsAll(map.getVisible(map.getPosition(new Coord(2,1)))));

        assertTrue(sandbox.tilesInRoom(map.room(map.getPosition(new Coord(2,2))))
                .containsAll(map.tilesInRoom(map.room(map.getPosition(new Coord(2,2))))));
    }

    @Test
    void testPawn(){
        //TODO complete the changes on Pawn and Player classes
        /*
        TileUID thisTile = map.getPosition(new Coord(1,1));

        DamageableUID[] listD = new DamageableUID[map.getDamageable().size()];
        map.getDamageable().toArray(listD);
        DamageableUID pawn1 = listD[0];

        map.addDamageable(thisTile, listD[0]);
        sandbox = map.createSandbox();

        System.out.println(sandbox.containedPawns(thisTile));
        assertEquals(thisTile , sandbox.tile(pawn1));
        assertEquals(Set.of(pawn1) , sandbox.containedPawns(thisTile));
        map.removeDamageable(thisTile, pawn1);
        assertFalse(sandbox.containedPawns(thisTile).isEmpty());
        assertTrue(map.containedPawns(thisTile).isEmpty());
        //assertTrue(map.containedPawns(tLeft).isEmpty());
        */

    }
}
