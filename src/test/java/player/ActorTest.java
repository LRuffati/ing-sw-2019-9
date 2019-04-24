package player;

import actions.utils.AmmoColor;
import board.GameMap;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    GameMap map;
    DamageableUID pawnid;

    @Test
    void firstConstructor(){
        Actor Pietro = new Actor(map, pawnid);
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


}
