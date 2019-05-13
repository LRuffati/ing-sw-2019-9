package player;


import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.AmmoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.GrabbableUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.lang.invoke.WrongMethodTypeException;
import java.security.InvalidParameterException;
import java.util.List;

import static actions.utils.AmmoColor.YELLOW;
import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    private GameMap map;
    private List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "src/resources/ammoTile.txt";
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, tilePath, 3);
        }
        catch (FileNotFoundException e){
        }
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

            assertFalse(Pietro.isTurn());

            assertEquals(Pietro.getPawn(), Pietro.getGm().getPawn(Pietro.getPawn().getDamageableUID()));
            assertEquals(Pietro, Pietro.getPawn().getActor());
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
        Pietro.setTurn(true);
        Pietro.move(map.getPosition(new Coord(1,2)));
        assertEquals(map.getPosition(new Coord(1,2)), Pietro.getPawn().getTile());
        Pietro.setFrenzy();
        Coord c = new Coord(2,3);
        Pietro.move(map.getPosition(c));
        assertEquals(map.getPosition(c), Pietro.getPawn().getTile());
    }

    @Test
    void unconditionalMovement() {
        Actor Pietro = actorList.get(0);
        Pietro.setTurn(false);
        Pietro.move(map.getPosition(new Coord(1,1)));
        assertEquals(map.getPosition(new Coord(1,1)), Pietro.getPawn().getTile());
    }

    @Test
    void addMarkTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Actor Lorenzo = actorList.get(2);

        //melo gives 3 marks to pietro
        assertEquals(3, Pietro.addMark(melo.pawnID(),4));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        //melo gives 2 marks to pietro
        assertEquals(0, Pietro.addMark(melo.pawnID(), 2));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        //melo gives 1 mark to lorenzo
        assertEquals(0, Lorenzo.addMark(melo.pawnID(), 1));

        //lorenzo gives 2 marks to pietro
        assertEquals(1, Pietro.addMark(Lorenzo.pawnID(), 1));
        assertEquals(1, Pietro.addMark(Lorenzo.pawnID(), 1));
        assertEquals(2, Pietro.getMarks().get(Lorenzo.pawnID()));
        assertEquals(3, Pietro.getMarks().get(melo.pawnID()));

        assertEquals(3, melo.numOfMarksApplied());
        assertEquals(2, Lorenzo.numOfMarksApplied());
        assertEquals(0, Pietro.numOfMarksApplied());
    }

    @Test
    void addDamageTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);

        Pietro.move(map.getPosition(new Coord(0,0)));

        Pietro.addDamage(melo,2);
        assertTrue(Pietro.getDamageTaken().contains(melo));
        assertEquals(2, Pietro.getDamageTaken().size());
        assertFalse(Pietro.isDead());

        Pietro.addMark(melo.pawnID(), 1);
        assertEquals(1, Pietro.getMarks().get(melo.pawnID()));

        Pietro.addDamage(melo, 3);
        assertEquals(0, Pietro.getMarks().get(melo.pawnID()));
        assertEquals(6, Pietro.getDamageTaken().size());

        assertFalse(Pietro.isDead());
        assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));


        Pietro.addMark(melo.pawnID(), 3);
        Pietro.addDamage(melo, 5);
        assertEquals(0, Pietro.getMarks().get(melo.pawnID()));
        assertEquals(Pietro.hp()+1, Pietro.getDamageTaken().size());
        assertTrue(Pietro.isDead());

        Pietro.respawn(YELLOW);
    }

    //TODO: valid behaviour?
    @Test
    void testAddDamage0(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.addMark(melo.pawnID(), 2);
        Pietro.addDamage(melo, 0);
        assertEquals(2, Pietro.getMarks().get(melo.pawnID()));
        assertEquals(0, Pietro.getDamageTaken().size());
    }

    @Test
    void addDamageTest2(){
        Actor a1 = actorList.get(0);
        Actor a2 = actorList.get(1);
        Actor a3 = actorList.get(2);

        a1.addDamage(a2,1);
        a1.addDamage(a3,2);
        assertEquals(3, a1.getDamageTaken().size());
        a1.addMark(a2.pawnID(), 2);
        a1.addMark(a3.pawnID(), 3);
        a3.addMark(a2.pawnID(), 1);
        assertEquals(2, a1.getMarks().get(a2.pawnID()));
        assertEquals(3, a1.getMarks().get(a3.pawnID()));

        a1.addDamage(a3, 1);
        assertEquals(2, a1.getMarks().get(a2.pawnID()));
        assertEquals(0, a1.getMarks().get(a3.pawnID()));
        assertEquals(7, a1.getDamageTaken().size());

        a1.addDamage(a2, 1);
        assertEquals(0, a1.getMarks().get(a2.pawnID()));
        assertEquals(0, a1.getMarks().get(a3.pawnID()));
        assertEquals(10, a1.getDamageTaken().size());

        assertTrue(a1.isDead());

        a1.addDamage(a2, 1);
        assertEquals(a1.hp()+1, a1.getDamageTaken().size());
        assertTrue(a1.isDead());

        a1.addDamage(a2, 3);
        assertEquals(a1.hp()+1, a1.getDamageTaken().size());
    }

    @Test
    void respawnTest(){
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.respawn(AmmoColor.YELLOW);
        melo.respawn(AmmoColor.BLUE);

        assertTrue(map.getTile(map.getPosition(new Coord(2,3))).spawnPoint());
        assertTrue(map.getTile(Pietro.getPawn().getTile()).spawnPoint());

        assertEquals(map.getPosition(new Coord(2,3)), Pietro.getPawn().getTile());
        assertEquals(map.getPosition(new Coord(0,2)), melo.getPawn().getTile());

        assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));
        Pietro.addDamage(actorList.get(1), 10);
        assertFalse(Pietro.getDamageTaken().isEmpty());

        Pietro.respawn(YELLOW);
        assertTrue(Pietro.getDamageTaken().isEmpty());
    }

    @Test
    void ammoPickUpTest(){
        AmmoCard am = new AmmoCard(new AmmoAmount(),0);
        Actor Pietro = actorList.get(0);
        assertThrows(WrongMethodTypeException.class, () -> Pietro.pickUp(am), "It's not your turn");
        Pietro.setTurn(true);
        //TODO put the ammocard in a tile and check if the player grabs it.
    }
}
