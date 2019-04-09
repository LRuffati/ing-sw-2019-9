package player;

import board.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActorTest {

    GameMap map;

    @BeforeEach
    void setup(){

    }

    @Test
    void testActor(){
        Actor act = new Actor(map);
        assert(act.getPawn().getActor() == act);
    }
}
