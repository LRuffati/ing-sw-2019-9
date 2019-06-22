package gamemanager;

import grabbables.AmmoCard;
import grabbables.Deck;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

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

    private void setup2(){
        String path = "test/TileTest.txt";
        try {
            ammoCardCollection = ParserAmmoTile.parse(path);
            ammoCardDeck = new GameBuilder(null, null, null, path, 1).getDeckOfAmmoCard();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    void test(){
        assertEquals(36, ammoCardCollection.size());

        int size = 0;
        List<AmmoCard> list = new ArrayList<>();
        while(ammoCardDeck.hasNext()){
            size++;
            list.add(ammoCardDeck.next());
        }
        assertEquals(36-(12-2-3), size);

        for(AmmoCard ammoCard : list)
            ammoCardDeck.discard(ammoCard);


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

        int n = 0;
        while(ammoCardDeck.hasNext()){
            n++;
            ammoCardDeck.next();
        }

        assertThrows(NoSuchElementException.class, () -> ammoCardDeck.next());
    }

    @AfterAll
    static void forCompleteness(){
        ParserAmmoTile p = new ParserAmmoTile();
    }
}