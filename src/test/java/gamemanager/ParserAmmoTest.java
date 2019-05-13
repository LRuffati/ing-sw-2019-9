package gamemanager;

import grabbables.AmmoCard;
import grabbables.Deck;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ParserAmmoTest {

    private Deck<AmmoCard> ammoCardDeck;
    private Collection<AmmoCard> ammoCardCollection;

    @BeforeEach
    void setup(){
        String tilePath = ParserConfiguration.parsePath("ammoTilePath");
        try {
            ammoCardCollection = ParserAmmoTile.parse(tilePath);
            ammoCardDeck = new GameBuilder(null, null, null, null, 1).getDeckOfAmmoCard();
        }
        catch (FileNotFoundException e){
        }
    }

    void setup2(){
        String path = "src/test/java/gamemanager/TileTest";
        try {
            ammoCardCollection = ParserAmmoTile.parse(path);
            ammoCardDeck = new GameBuilder(null, null, null, path, 1).getDeckOfAmmoCard();
        }
        catch (FileNotFoundException e){
            System.out.println(e.getStackTrace());
        }
    }

    @Test
    void testWrongFile(){
        assertThrows(FileNotFoundException.class, () -> ParserAmmoTile.parse(""));
        assertThrows(FileNotFoundException.class,
                () -> new GameBuilder(null,null,null,"", 1));
    }

    @Test
    void test(){
        assertEquals(36, ammoCardCollection.size());

        AmmoCard card = ammoCardDeck.next();
        assertTrue(ammoCardDeck.isPicked(card));

        boolean res = false;
        for(AmmoCard c : ammoCardCollection){
            if(c.toString().equals(card.toString()))
                res = true;
        }
        assertTrue(res);

        Collection<AmmoCard> collCard = ammoCardDeck.take(10);
        ammoCardDeck.discard(card);
        assertFalse(ammoCardDeck.isPicked(card));
    }

    @Test
    void testAmmoCardGetter(){
        setup2();
        assertEquals(2, ammoCardCollection.size());

        AmmoCard c1 = ammoCardDeck.next();
        AmmoCard c2 = ammoCardDeck.next();
        assertFalse(c1.getAmmoAmount().compareTo(c2.getAmmoAmount()) > 0);
        assertNotEquals(c1.getNumOfPowerUp(), c2.getNumOfPowerUp());

        assertNotEquals(c1,c2);
    }

    @AfterAll
    static void forCompleteness(){
        ParserAmmoTile p = new ParserAmmoTile();
    }
}