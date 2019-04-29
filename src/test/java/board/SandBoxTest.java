package board;

import gamemanager.GameBuilder;
import org.junit.jupiter.api.*;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class SandBoxTest {
    private GameMap map;
    private Sandbox sandbox;


    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, null, 2);
        }
        catch (FileNotFoundException e){
        }
        map = builder.getMap();
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
        assertTrue(sandbox.allTiles().containsAll(map.allTiles())
                && map.allTiles().containsAll(sandbox.allTiles()));

        assertTrue(sandbox.tilesSeen(map.getPosition(new Coord(0,0))).containsAll(map.getVisible(map.getPosition(new Coord(0,0)))));
        assertTrue(sandbox.tilesSeen(map.getPosition(new Coord(2,1))).containsAll(map.getVisible(map.getPosition(new Coord(2,1)))));

        assertTrue(sandbox.tilesInRoom(map.room(map.getPosition(new Coord(2,2))))
                .containsAll(map.tilesInRoom(map.room(map.getPosition(new Coord(2,2))))));
    }

    @Test
    void testGetter(){
        assertTrue(sandbox.getRoom(map.room(map.getPosition(new Coord(0,0)))).containedTiles()
                .containsAll(map.tilesInRoom(map.room(map.getPosition(new Coord(0,0))))));
        assertTrue(map.tilesInRoom(map.room(map.getPosition(new Coord(0,0))))
                .containsAll(sandbox.getRoom(map.room(map.getPosition(new Coord(0,0)))).containedTiles()));

        assertEquals(map.getPosition(new Coord(1,1)) , sandbox.getTile(map.getPosition(new Coord(1,1))).location());
    }

    @Test
    void testPawn(){
        TileUID thisTile;

        DamageableUID[] listD = new DamageableUID[map.getDamageable().size()];
        map.getDamageable().toArray(listD);
        Pawn pawn1 = map.getPawn(listD[0]);

        try {
            thisTile = map.getPosition(new Coord(1,1));
            pawn1.move(thisTile);
            allControls(pawn1, thisTile);

            thisTile = map.getPosition(new Coord(2,2));
            pawn1.move(thisTile);
            allControls(pawn1, thisTile);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void allControls(Pawn pawn1, TileUID thisTile){
        sandbox = map.createSandbox();

        assertEquals(thisTile , sandbox.getBasic(pawn1.getDamageableUID()).location());

        assertEquals(map.room(pawn1.getTile()) , sandbox.room(pawn1.getDamageableUID()));
        assertEquals(thisTile , sandbox.tile(pawn1.getDamageableUID()));

        for(TileUID t : map.allTiles()){
            if(!t.equals(thisTile))
                assertTrue(map.containedPawns(t).isEmpty());
            else
                assertTrue(sandbox.containedPawns(thisTile).contains(pawn1.getDamageableUID())
                        && sandbox.containedPawns(thisTile).size() == 1);
        }

    }
}
