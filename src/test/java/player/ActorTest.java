package player;

import actions.utils.AmmoColor;
import board.GameMap;
import grabbables.Grabbable;
import grabbables.Weapon;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.TileUID;

import java.lang.invoke.WrongMethodTypeException;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    GameMap map;
    DamageableUID pawnid;
    TileUID t;

    @Test
    void firstConstructor(){
        Actor Pietro = new Actor(map);
        assertEquals(0, Pietro.getPoints());
        assertEquals(0, Pietro.getNumOfDeaths());
        assertEquals(pawnid, Pietro.getPawn().getDamageableUID());
        assertEquals(Pietro, Pietro.getPawn().getActor());
        assertFalse(Pietro.isTurn());
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.BLUE));
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.RED));
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.YELLOW));
        assertFalse(Pietro.getFrenzy());
        assertNull(Pietro.getMarks());
        assertEquals(map, Pietro.getGm());
    }

    @Test
    void secondConstructor(){
        Actor Pietro = new Actor(map, pawnid, true);
        assertEquals(0, Pietro.getPoints());
        assertEquals(0, Pietro.getNumOfDeaths());
        assertEquals(pawnid, Pietro.getPawn().getDamageableUID());
        assertEquals(Pietro, Pietro.getPawn().getActor());
        assertTrue(Pietro.isTurn());
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.BLUE));
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.RED));
        assertEquals(1,Pietro.getAmmo().getAmounts().get(AmmoColor.YELLOW));
        assertFalse(Pietro.getFrenzy());
        assertNull(Pietro.getMarks());
        assertEquals(map, Pietro.getGm());
    }

    @Test
    void unconditionalMovement(){
        Actor Pietro = new Actor(map);
        Pietro.setTurn(false);
        Pietro.unconditionalMove(t);
        assertEquals(t, Pietro.getPawn().getTile());
    }

    @Test
    void falseTurnPickUP(){
        Actor Pietro = new Actor(map);
        Weapon w = new Weapon();
        Weapon toRemove = new Weapon();
        Pietro.setTurn(false);
        assertThrows(WrongMethodTypeException.class, () -> {
            Pietro.pickUp(w, toRemove);
        });
    }

}
