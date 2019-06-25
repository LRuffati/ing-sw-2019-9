package gamemanager;

import controller.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class DominationTest {


    GameBuilder game = null;

    @BeforeEach
    void setup() {
        try {
            game = new GameBuilder(GameMode.DOMINATION, null, null, null,null, 3);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test() {
        assertEquals(3, game.getActorList().size());
        assertEquals(6, game.getMap().getDamageable().size());
        assertTrue(game.getScoreboard() instanceof DominationMode);
    }
}
