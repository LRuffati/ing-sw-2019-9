package gamemanager;

import org.junit.jupiter.api.Test;

import player.Actor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ScoreboardTest {
    List<Actor> listaAttori;

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
        //TODO: Carmelo, fix please
        Actor pietro = new Actor();
        listaAttori = new ArrayList<>();
        listaAttori.add(pietro);
        Scoreboard sb = new Scoreboard(listaAttori);
        //pietro.getPawn().removeFromMap();
    }
}
