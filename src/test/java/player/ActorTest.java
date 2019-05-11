package player;


import actions.utils.AmmoColor;
import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uid.DamageableUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.lang.invoke.WrongMethodTypeException;
import java.security.InvalidParameterException;
import java.util.List;

import static actions.utils.AmmoColor.YELLOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActorTest {

    GameMap map;
    List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "src/resources/ammoTile.txt";
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, tilePath, 2);
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
    void nullConstructorTest(){
        /*Actor melo = new Actor();
        assertNull(melo.getNullPawn());*/
    }

    @Test
    void moveTest() throws NoSuchFieldException{
        Actor Pietro = actorList.get(0);
        Pietro.setTurn(true);
        Pietro.move(map.getPosition(new Coord(1,1)));
        Pietro.move(map.getPosition(new Coord(1, 2)));
        assertEquals(map.getPosition(new Coord(1,2)), Pietro.getPawn().getTile());
        Pietro.setFrenzy();
        Coord c = new Coord(2,3);
        Pietro.move(map.getPosition(c));
        assertEquals(map.getPosition(c), Pietro.getPawn().getTile());
    }

    @Test
    void unconditionalMovement() throws NoSuchFieldException {
        Actor Pietro = actorList.get(0);
        Pietro.setTurn(false);
        Pietro.move(map.getPosition(new Coord(1,1)));
        assertEquals(map.getPosition(new Coord(1,1)), Pietro.getPawn().getTile());
    }

    @Test
    void gettingDmgTest(){
        //TODO Incomplete test, review the method getDMG(Actor actor).
        /*Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        Pietro.getDMG(melo);
        assertTrue(Pietro.getDamageTaken().contains(melo));
        assertEquals(0, Pietro.getMarks().get(melo.getPawn().getDamageableUID()));
        */
    }

    //@Test
        //TODO: needs weapon managment

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
        /*
        Actor Pietro = actorList.get(0);
        Actor Lore = actorList.get(1);

        assertEquals(1, Pietro.addMark(Lore.getPawn().getDamageableUID(),1));
        */
    }



    @Test
    void respawnTest(){
        //TODO Pietro please fix the getTile() exception.
        /*
        Actor Pietro = actorList.get(0);
        Pietro.getPawn().move(map.getPosition(new Coord(1,1)));
        assertThrows(InvalidParameterException.class, ()-> Pietro.respawn(YELLOW));
        TileUID t = null;
        Pietro.getPawn().move(t);
        Pietro.respawn(YELLOW);
        assertTrue(Pietro.getDamageTaken().isEmpty());

        */
    }
}
