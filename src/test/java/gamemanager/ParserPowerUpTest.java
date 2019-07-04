package gamemanager;

import actions.utils.PowerUpType;
import grabbables.Deck;
import grabbables.PowerUp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class ParserPowerUpTest {

    private Deck<PowerUp> powerUpDeck;
    private Collection<PowerUp> powerUpCollection;

    @BeforeEach
    void setup() {
        String powerUpPath = ParserConfiguration.parsePath("powerUpPath");
        powerUpCollection = ParserPowerUp.parse(powerUpPath);
        powerUpDeck = new GameBuilder(null, null, powerUpPath, null, 1).getDeckOfPowerUp();
    }

    private void setup2() {
        String path = "test/PowerUpTestFile";
        powerUpCollection = ParserPowerUp.parse(path);
        powerUpDeck = new GameBuilder(null, null, path, null, 1).getDeckOfPowerUp();
    }

    @Test
    void test(){
        assertEquals(24, powerUpCollection.size());

        PowerUp card = powerUpDeck.next();
        assertTrue(powerUpDeck.isPicked(card));

        boolean res = false;
        for(PowerUp c : powerUpCollection){
            if(c.toString().equals(card.toString()))
                res = true;
        }
        assertTrue(res);

        Collection<PowerUp> collCard = powerUpDeck.take(10);
        powerUpDeck.discard(card);
        assertFalse(powerUpDeck.isPicked(card));
    }

    @Test
    void testGetter(){
        setup2();
        assertEquals(4, powerUpCollection.size());

        PowerUp c1 = powerUpDeck.next();
        PowerUp c2 = powerUpDeck.next();
        PowerUp c3 = powerUpDeck.next();
        assertEquals(PowerUpType.NEWTON, c1.getType());
        assertEquals(PowerUpType.NEWTON, c2.getType());
        assertEquals(c1.getType() , c2.getType());

        assertNotEquals(c1,c2);
    }

    @AfterAll
    static void forCompleteness(){
        ParserPowerUp p = new ParserPowerUp();
    }
}
