package player;

import actions.utils.AmmoColor;
import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {

    DamageableUID dUID;
    TileUID tUID;
    GameMap map;
    Actor attore;
    Pawn pietro;
    List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/ammoTile.txt";
        String mapPath = "C:/Users/pietr/Desktop/Polimi/anno3/periodo2/IngSw/resources/map1.txt";
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
    void firstConstructor(){
        for(Actor Pietro : actorList) {
            assertEquals(0, Pietro.getPoints());
            assertEquals(0, Pietro.getNumOfDeaths());
            assertEquals(1, Pietro.getAmmo().getAmounts().get(AmmoColor.BLUE));
            assertEquals(1, Pietro.getAmmo().getAmounts().get(AmmoColor.RED));
            assertEquals(1, Pietro.getAmmo().getAmounts().get(AmmoColor.YELLOW));
            assertFalse(Pietro.getFrenzy());
            assertNull(Pietro.getMarks());
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
    void unconditionalMovement(){
        Actor Pietro = actorList.get(0);
        Pietro.setTurn(false);
        Pietro.unconditionalMove(map.getPosition(new Coord(1,1)));
        assertEquals(map.getPosition(new Coord(1,1)), Pietro.getPawn().getTile());
    }

    @Test
    void falseTurnPickUP(){
        //TODO: needs weapon managment
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

}
