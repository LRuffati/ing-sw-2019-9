package player;

import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    private DamageableUID dUID;
    private TileUID tUID;
    private GameMap map;
    private Actor attore;
    private Pawn pietro;
    private List<Actor> actorList;

    @BeforeEach
    void setup() {
        GameBuilder builder = null;
        String tilePath = "ammoTile.txt";
        String mapPath = "map1.txt";
        builder = new GameBuilder(
                mapPath, null, null, tilePath, 2);
        map = builder.getMap();
        actorList = builder.getActorList();
    }

    @Test
    void constrTest(){
        pietro = new Pawn(dUID, tUID, map);
        assertEquals(tUID, pietro.getTile());
        assertEquals(dUID, pietro.getDamageableUID());
        assertNull(pietro.getActor());
        assertEquals(map, pietro.getMap());
    }

    @Test
    void moveTest() throws NoSuchFieldException {
        pietro = actorList.get(0).pawn();
        TileUID tomove;
        final TileUID finalTomove;
        finalTomove = map.getPosition(new Coord(0,0));
        tomove = map.getPosition(new Coord(0,0));
        pietro.move(tomove);
        for(TileUID t : map.allTiles())
            if(!t.equals(tomove))
                assertFalse(map.containedPawns(t).contains(pietro.getDamageableUID()));
        assertTrue(map.containedPawns(tomove).contains(pietro.getDamageableUID()));

        tomove = map.getPosition(new Coord(2,2));
        pietro.move(tomove);
        for(TileUID t : map.allTiles())
            if(!t.equals(tomove))
                assertFalse(map.containedPawns(t).contains(pietro.getDamageableUID()));
        assertTrue(map.containedPawns(tomove).contains(pietro.getDamageableUID()));
    }

    @Test
    void deadPawnTest(){
        pietro = actorList.get(0).pawn();
        for(TileUID t : map.allTiles())
            assertFalse(map.containedPawns(t).contains(pietro.getDamageableUID()));

        TileUID tile = map.getPosition(new Coord(1,1));
        pietro.move(tile);
        for(TileUID t : map.allTiles())
            if(!t.equals(tile))
                assertFalse(map.containedPawns(t).contains(pietro.getDamageableUID()));
        assertTrue(map.containedPawns(tile).contains(pietro.getDamageableUID()));
    }

    @Test
    void viewTest() {
        ActorView actorView = actorList.get(0).pawn().generateView(map.generateView(actorList.get(0).pawnID()), actorList.get(0).pawnID(), actorList.get(0).pawnID());
        assertEquals(actorList.get(0).getPoints(), actorView.score());
    }
}
