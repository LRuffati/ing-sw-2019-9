package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import board.Coord;
import board.GameMap;
import exception.AmmoException;
import gamemanager.GameBuilder;
import grabbables.AmmoCard;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Actor;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActorViewTest {
    private GameBuilder gameBuilder;
    private GameMap map;
    private List<Actor> actors;


    @BeforeEach
    void setup(){
        gameBuilder = null;
        try {
            gameBuilder = new GameBuilder(null, null, null, null, 2);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        map = gameBuilder.getMap();
        actors = gameBuilder.getActorList();
    }

    @Test
    void testActor() {
        Actor act1 = actors.get(0);
        Actor act2 = actors.get(1);
        act1.pawn().setUsername("Giocatore uno");
        act2.pawn().setUsername("Giocatore due");


        act1.addDamage(act2, 2);
        act1.addPoints(10);
        act1.setTurn(true);
        act1.move(map.getPosition(new Coord(0,0)));
        act1.pickUp((AmmoCard)map.getGrabbable(map.getPosition(new Coord(0,0))).iterator().next());
        act1.move(map.getPosition(new Coord(1,1)));
        act1.pickUp((AmmoCard)map.getGrabbable(map.getPosition(new Coord(1,1))).iterator().next());
        act1.move(map.getPosition(new Coord(2,2)));
        act1.pickUp((AmmoCard)map.getGrabbable(map.getPosition(new Coord(2,2))).iterator().next());

        try {
            act1.move(map.getPosition(new Coord(1,0)));
            //Weapon weapon = (Weapon)map.getGrabbable(map.getPosition(new Coord(1,0))).iterator().next();
            //System.out.println(act1.getAmmo() + "\n" + weapon.getBuyCost() + "\n" + act1.getPowerUp().size());
            act1.pickUp((Weapon) map.getGrabbable(map.getPosition(new Coord(1,0))).iterator().next(),
                    null, null);
        }
        catch (AmmoException e){
            e.printStackTrace();
        }



        ActorView view1 = act1.pawn().generateView(map.generateView(act1.pawnID()), true);
        ActorView view2 = act2.pawn().generateView(map.generateView(act1.pawnID()), false);

        assertTrue(view2.damageTaken().isEmpty());

        assertEquals(2, view1.damageTaken().size());
        for(ActorView a : view1.damageTaken()) {
            assertEquals(view2.name(), a.name());
        }

        //assertEquals(Map.of(AmmoColor.YELLOW,1, AmmoColor.BLUE,1, AmmoColor.RED,1), view1.ammo());
        assertEquals(Map.of(AmmoColor.YELLOW,1, AmmoColor.BLUE,1, AmmoColor.RED,1), view2.ammo());

        assertEquals(10, view1.score());
        assertEquals(-1, view2.score());

        assertEquals(1, view1.loadedWeapon().size());
        assertNull(view2.loadedWeapon());
    }
}
