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
                    mapPath, null, null, tilePath, 2);
        }
        catch (FileNotFoundException e){
        }
        map = builder.getMap();
        actorList = builder.getActorList();
    }

    @Test
    void fullConstructor(){
        Actor pietro = new Actor();
        listaAttori = new ArrayList<>();
        listaAttori.add(pietro);
        Scoreboard sb = new Scoreboard(listaAttori, 5);
        assertEquals(listaAttori, sb.getActorsList());
        assertEquals(5,sb.getMaxDeaths());
        assertEquals(0, sb.getNumOfDeaths());
        assertFalse(sb.finalFrenzy());
    }

    @Test
    void stdConstructor(){
        Actor pietro = new Actor();
        listaAttori = new ArrayList<>();
        listaAttori.add(pietro);
        Scoreboard sb = new Scoreboard(listaAttori);
        assertEquals(listaAttori, sb.getActorsList());
        assertEquals(8,sb.getMaxDeaths());
        assertEquals(0, sb.getNumOfDeaths());
    }

    @Test
    void dumbConstructor(){
        Scoreboard sb = new Scoreboard();
        assertEquals(0,sb.getMaxDeaths());
    }

    @Test
    void scoringTest(){
        //TODO Pietro please fix the getTile() exception.
        Scoreboard sb = new Scoreboard(actorList);
        Actor Pietro = actorList.get(0);
        Pietro.getPawn().move(null);
        sb.score(Pietro);
        assertEquals(1, Pietro.getDamageTaken().get(0).getPoints());

    }
}
