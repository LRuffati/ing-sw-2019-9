package test;

import deck.Deck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TestClass<T extends Iterable<T>> implements Iterator<T> {

    private Collection<T> deckCollection;

    //Checking what happens when stash has less than maxN elements:
    @Test
    public void test(){


        deckCollection = new ArrayList<>();
        Deck deckTest = new Deck(deckCollection);
        //deckCollection.add();
        assert deckTest.hasNext();
    }

}