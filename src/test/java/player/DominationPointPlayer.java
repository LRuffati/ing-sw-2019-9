package player;

import board.Coord;
import board.GameMap;
import controller.GameMode;
import gamemanager.DominationMode;
import gamemanager.GameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.List;

class DominationPointPlayer {

    private GameMap map;
    private List<Actor> actorList;
    private ControlPointActor dom;
    private DominationMode scoreboard;

    @BeforeEach
    void setup(){
        GameBuilder builder = new GameBuilder(GameMode.DOMINATION, 4);
        map = builder.getMap();
        actorList = builder.getActorList();
        dom = (ControlPointActor)builder.getDominationPointActor().get(new Coord(1,0));
        scoreboard = (DominationMode)builder.getScoreboard();
    }

    @Test
    void testActor() {
        dom.damage(actorList.get(0),2);
        dom.damageRaw(actorList.get(1), 1);
        dom.addMark(actorList.get(2), 1);
        dom.steppedOn(actorList.get(3));
        dom.endTurn(actorList.get(1), scoreboard);
        assertEquals(2, scoreboard.getSpawnTracker().get(Color.red).size());
        assertFalse(scoreboard.finalFrenzy());
        scoreboard.claimWinner();
        assertEquals(8, actorList.get(1).getPoints());

        dom.damage(actorList.get(0),2);
        dom.steppedOn(actorList.get(0));
        dom.endTurn(actorList.get(0), scoreboard);
        scoreboard.claimWinner();
        assertEquals(6, actorList.get(0).getPoints());
        assertEquals(14, actorList.get(1).getPoints());
    }


}
