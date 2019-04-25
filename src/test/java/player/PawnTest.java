package player;

import board.GameMap;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.TileUID;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    DamageableUID dUID;
    TileUID tUID;
    GameMap map;
    Actor attore;
    Pawn pietro;

    @Test
    void constrTest(){
        pietro = new Pawn(dUID, tUID, map);
        assertEquals(tUID, pietro.getTile());
        assertEquals(dUID, pietro.getDamageableUID());
        assertNull(pietro.getActor());
        assertEquals(map, pietro.getMap());
    }

    @Test
    void domPointConstrTest(){
        pietro = new Pawn();
        assertNull(pietro.getMap());
    }

    @Test
    void bindingTest(){
        pietro = new Pawn(dUID,tUID,map);
        attore = new Actor();
        pietro.setBinding(attore);
        assertEquals(attore,pietro.getActor());
    }

    @Test
    void moveTest(){
        pietro = new Pawn(dUID,tUID,map);
        TileUID tomove = new TileUID();
        pietro.move(tomove);
        assertFalse(map.containedPawns(tUID).contains(pietro.getDamageableUID()));
        assertTrue(map.containedPawns(tomove).contains(pietro.getDamageableUID()));
    }

}
