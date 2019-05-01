package player;


import actions.utils.AmmoColor;
import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.TileUID;

import java.io.FileNotFoundException;
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
        Pietro.move(map.getPosition(new Coord(1,1)));
        Pietro.movePlayer(map.getPosition(new Coord(1, 2)));
        assertEquals(map.getPosition(new Coord(1,2)), Pietro.getPawn().getTile());
        Pietro.setFrenzy();
        Coord c = new Coord(2,3);
        Pietro.movePlayer(map.getPosition(c));
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
    void addDamageTest(){
        //TODO Incomplete test.
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.addDamage(melo,2);
        assertTrue(Pietro.getDamageTaken().contains(melo));
        Pietro.addMark(melo.pawnID(), 1);
        //TODO the following assert should be false.
        assertEquals(1, Pietro.getMarks().get(melo.getPawn().getDamageableUID()));
    }

    @Test
    //TODO: needs weapon management
    void falseTurnPickUP(){
        /*
        Actor Pietro = new Actor(map);
        Weapon w = new Weapon();
        Weapon toRemove = new Weapon();
        Pietro.setTurn(false);
        assertThrows(WrongMethodTypeException.class, () -> {
            Pietro.pickUp(w, toRemove);
        });
        */
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
    void respawnTest(){
        assertTrue(map.allTiles().size() == 10);
        actorList.get(0).move(map.getPosition(new Coord(0,3)));

        assertFalse(map.allTiles().contains(map.getPosition(new Coord(0,3))));
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.respawn(AmmoColor.YELLOW);
        Pietro.respawn(AmmoColor.BLUE);
        assertEquals(map.getPosition(new Coord(3,1)), Pietro.getPawn().getTile());
        assertEquals(map.getPosition(new Coord(0,2)), melo.getPawn().getTile());

        Pietro.getPawn().move(map.getPosition(new Coord(1,1)));
        Pietro.addDamage(actorList.get(1), 10);
        //assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));

        Pietro.respawn(YELLOW);
        assertTrue(Pietro.getDamageTaken().isEmpty());
    }
}
