package gamemanager;

import board.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import player.Actor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreboardTest {
    List<Actor> listaAttori;
    GameMap map;
    List<Actor> actorList;

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
    void fullConstructor(){
        Actor pietro = new Actor(null,null,false);
        listaAttori = new ArrayList<>();
        listaAttori.add(pietro);
        Scoreboard sb = new Scoreboard(listaAttori, 5);
        assertEquals(listaAttori, sb.getActorsList());
        assertEquals(5,sb.getMaxDeaths());
        assertEquals(0, sb.getSkullBox().size());
        assertFalse(sb.finalFrenzy());
    }

    @Test
    void stdConstructor(){
        Actor pietro = new Actor(null,null,false);
        listaAttori = new ArrayList<>();
        listaAttori.add(pietro);
        Scoreboard sb = new Scoreboard(listaAttori);
        assertEquals(listaAttori, sb.getActorsList());
        assertEquals(8,sb.getMaxDeaths());
        assertEquals(0, sb.getSkullBox().size());
    }

    @Test
    void dumbConstructor(){
        Scoreboard sb = new Scoreboard();
        assertEquals(0,sb.getMaxDeaths());
    }

    @Test
    void scoringTest(){
        Scoreboard sb = new Scoreboard(actorList);
        Actor Pietro = actorList.get(0);
        Actor Lorenzo = actorList.get(1);
        Actor Carmelo = actorList.get(2);

        //TODO: add tests and verify other cases
        Pietro.addDamage(Carmelo, 1);
        for(int i=0; i<10;i++)
            Pietro.addDamage(Lorenzo, 1);
        sb.score(Pietro);

        assertEquals(7, Pietro.getDamageTaken().get(0).getPoints());
        assertEquals(8, Pietro.getDamageTaken().get(1).getPoints());

        assertEquals(Carmelo , Pietro.getDamageTaken().get(0));
        assertEquals(Lorenzo , Pietro.getDamageTaken().get(1));
    }
}
