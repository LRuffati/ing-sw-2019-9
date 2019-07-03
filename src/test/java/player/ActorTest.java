
package player;


import actions.effects.Effect;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import board.Coord;
import board.GameMap;
import controller.SetMessageProxy;
import controller.controllermessage.ControllerMessage;
import gamemanager.GameBuilder;
import grabbables.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static actions.utils.AmmoColor.*;
import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    private GameMap map;
    private List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "ammoTile.txt";
        String mapPath = "map1.txt";
        builder = new GameBuilder(mapPath, null, null, tilePath, 3);
        map = builder.getMap();
        actorList = builder.getActorList();
    }

    @Test
    void constructorTest(){
        for(Actor Pietro : actorList) {
            assertEquals(0, Pietro.getPoints());
            assertEquals(0, Pietro.getNumOfDeaths());
            assertEquals(1, Pietro.getAmmo().getAmounts().get(AmmoColor.BLUE));
            assertEquals(1, Pietro.getAmmo().getAmounts().get(AmmoColor.RED));
            assertEquals(1, Pietro.getAmmo().getAmounts().get(YELLOW));
            assertFalse(Pietro.getFrenzy());
            assertTrue(Pietro.getMarks().isEmpty());
            assertEquals(map, Pietro.getGm());

            assertEquals(Pietro.pawn(), Pietro.getGm().getPawn(Pietro.pawn().getDamageableUID()));
            assertEquals(Pietro, Pietro.pawn().getActor());
        }

        int n = 0;
        for(Actor a : actorList){
            n += a.getFirstPlayer() ? 1 : 0;
        }
        assertEquals(1, n);
    }

    @Test
    void moveTest() {
        Actor Pietro = actorList.get(0);
        Pietro.move(map.getPosition(new Coord(1,2)));
        assertEquals(map.getPosition(new Coord(1,2)), Pietro.pawn().getTile());
        Coord c = new Coord(2,3);
        Pietro.move(map.getPosition(c));
        assertEquals(map.getPosition(c), Pietro.pawn().getTile());
    }

    @Test
    void unconditionalMovement() {
        Actor Pietro = actorList.get(0);
        Pietro.move(map.getPosition(new Coord(1,1)));
        assertEquals(map.getPosition(new Coord(1,1)), Pietro.pawn().getTile());
    }

    @Test
    void addMarkTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Actor Lorenzo = actorList.get(2);

        //melo gives 3 marks to pietro
        assertEquals(3, Pietro.addMark(melo,4));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        //melo gives 2 marks to pietro
        assertEquals(0, Pietro.addMark(melo, 2));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        //melo gives 1 mark to lorenzo
        assertEquals(1, Lorenzo.addMark(melo, 1));

        //melo gives 1 mark to Pietro
        assertEquals(0, Pietro.addMark(melo,1));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        //lorenzo gives 2 marks to pietro
        assertEquals(1, Pietro.addMark(Lorenzo, 1));
        assertEquals(1, Pietro.addMark(Lorenzo, 1));
        assertEquals(2, Pietro.getMarks().get(Lorenzo.pawnID()));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        assertEquals(4, melo.numOfMarksApplied());
        assertEquals(2, Lorenzo.numOfMarksApplied());
        assertEquals(0, Pietro.numOfMarksApplied());
    }

    @Test
    void addDamageTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);

        Pietro.move(map.getPosition(new Coord(0,0)));

        Pietro.damage(melo,2);
        assertTrue(Pietro.getDamageTaken().contains(melo));
        assertEquals(2, Pietro.getDamageTaken().size());
        assertFalse(Pietro.isDead());

        Pietro.addMark(melo, 1);
        assertEquals(1, Pietro.getMarks().get(melo.pawnID()));

        Pietro.damage(melo, 3);
        assertNull(Pietro.getMarks().get(melo.pawnID()));
        assertEquals(6, Pietro.getDamageTaken().size());

        assertFalse(Pietro.isDead());
        assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));


        Pietro.addMark(melo, 3);
        Pietro.damage(melo, 5);
        assertNull(Pietro.getMarks().get(melo.pawnID()));
        assertEquals(Pietro.getDamageTaken().size(), Pietro.getDamageTaken().size());
        assertTrue(Pietro.isDead());

        Pietro.respawn(YELLOW);
    }

    //TODO: valid behaviour?
    @Test
    void testAddDamage0(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.addMark(melo, 2);
        Pietro.damage(melo, 0);
        assertNull(Pietro.getMarks().get(melo.pawnID()));
        assertEquals(2, Pietro.getDamageTaken().size());
    }

    @Test
    void addDamageTest2(){
        Actor a1 = actorList.get(0);
        Actor a2 = actorList.get(1);
        Actor a3 = actorList.get(2);

        a1.damage(a2,1);
        a1.damage(a3,2);
        assertEquals(3, a1.getDamageTaken().size());
        a1.addMark(a2, 2);
        a1.addMark(a3, 3);
        a3.addMark(a2, 1);
        assertEquals(2, a1.getMarks().get(a2.pawnID()));
        assertEquals(3, a1.getMarks().get(a3.pawnID()));

        a1.damage(a3, 1);
        assertEquals(2, a1.getMarks().get(a2.pawnID()));
        assertNull(a1.getMarks().get(a3.pawnID()));
        assertEquals(7, a1.getDamageTaken().size());

        a1.damage(a2, 1);
        assertNull(a1.getMarks().get(a2.pawnID()));
        assertNull(a1.getMarks().get(a3.pawnID()));
        assertEquals(10, a1.getDamageTaken().size());

        assertTrue(a1.isDead());

        a1.damage(a2, 1);
        assertEquals(a1.getDamageTaken().size(), a1.getDamageTaken().size());
        assertTrue(a1.isDead());

        a1.damage(a2, 3);
        assertEquals(a1.getDamageTaken().size(), a1.getDamageTaken().size());
    }

    @Test
    void respawnTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.respawn(AmmoColor.YELLOW);
        melo.respawn(AmmoColor.BLUE);

        assertTrue(map.getTile(map.getPosition(new Coord(2,3))).spawnPoint());
        assertTrue(map.getTile(Pietro.pawn().getTile()).spawnPoint());

        assertEquals(map.getPosition(new Coord(2,3)), Pietro.pawn().getTile());
        assertEquals(map.getPosition(new Coord(0,2)), melo.pawn().getTile());

        assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));
        Pietro.damage(actorList.get(1), 10);
        assertFalse(Pietro.getDamageTaken().isEmpty());

        Pietro.respawn(YELLOW);
        assertTrue(Pietro.getDamageTaken().isEmpty());
    }

    @Test
    void ammoPickUpTest(){
        Actor Pietro = actorList.get(0);
        Pietro.pay(new AmmoAmount(Map.of(YELLOW,1, RED,1)));
        assertEquals(new AmmoAmount(Map.of(BLUE,1)).toString(), Pietro.getAmmo().toString());

        PowerUp p = new PowerUp(PowerUpType.TELEPORTER, BLUE) {
            @Override
            public boolean canUse(List<Effect> lastEffects) {
                return false;
            }

            @Override
            public ControllerMessage usePowup(SetMessageProxy pov, List<Effect> lastEffects, Runnable onPowerupFinalized) {
                return null;
            }
        };
        Pietro.pay(p);
        assertFalse(Pietro.getPowerUp().contains(p));

        assertFalse(Pietro.endTurn(Pietro, null));



    }

    @Test
    void actionTest() {
        Actor Pietro = actorList.get(0);

        assertEquals(2, Pietro.getActions().size());
        assertEquals(3, Pietro.getActions().get(0).size());

        Pietro.beginFF(true);
        assertTrue(Pietro.getFrenzy());
        assertEquals(1, Pietro.getActions().size());
        assertEquals(2, Pietro.getActions().get(0).size());

        Pietro.beginFF(false);
        assertEquals(2, Pietro.getActions().size());
        assertEquals(3, Pietro.getActions().get(0).size());
    }

    @Test
    void testPowerUp() {
        Actor Pietro = actorList.get(0);
        assertEquals(0, Pietro.getPowerUp().size());
        Pietro.drawPowerUpRaw(3);
        assertEquals(3, Pietro.getPowerUp().size());
        Pietro.discardPowerUp(Pietro.getPowerUp().iterator().next());
        assertEquals(2, Pietro.getPowerUp().size());
    }

    @Test
    void testDamagedBy() {
        Actor a = actorList.get(0);
        Actor b = actorList.get(1);
        a.damage(b, 1);
        assertTrue(a.getDamagedBy().contains(b));
    }
}