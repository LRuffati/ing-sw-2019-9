package gamemanager;

import board.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import player.Actor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class ScoreboardTest {
    private List<Actor> listaAttori;
    private GameMap map;
    private List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        try {
            builder = new GameBuilder(
                    null, null, null, null, 3);
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
        Scoreboard sb = new Scoreboard(listaAttori,8);
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
        Scoreboard sb = new Scoreboard(actorList,8);
        Actor Pietro = actorList.get(0);
        Actor Lorenzo = actorList.get(1);
        Actor Carmelo = actorList.get(2);

        //TODO: add tests and verify other cases
        Pietro.damage(Carmelo, 1);
        for(int i=0; i<10;i++)
            Pietro.damage(Lorenzo, 1);
        sb.score(Pietro);

        assertEquals(7, Pietro.getDamageTaken().get(0).getPoints());
        assertEquals(8, Pietro.getDamageTaken().get(1).getPoints());

        assertEquals(Carmelo , Pietro.getDamageTaken().get(0));
        assertEquals(Lorenzo , Pietro.getDamageTaken().get(1));
    }

    @Test
    void winnerClaimingTest(){
        Scoreboard sb = new Scoreboard(actorList,8);
        Actor Pietro = actorList.get(0);
        Pietro.addPoints(1);
        assertEquals(Pietro, sb.claimWinner());
    }

    @Test
    void killTest(){
        Scoreboard sb = new Scoreboard(actorList,8);
        Actor Pietro = actorList.get(0);
        Actor melo = actorList.get(1);
        sb.addKill(Pietro, melo);
        assertEquals(1,sb.getNumOfDeaths());
        //assertTrue(sb.getSkullBox().contains(Map.of(Pietro,2)));
    }
}
