package grabbables;

import genericitems.Tuple3;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    ArrayList<Integer> generateList(){
        Set<Integer> integerSet = new HashSet<>();
        Random random = new Random();
        Integer n = random.nextInt(255);
        for (int i = 0; i < n; i++)
        {
            integerSet.add(random.nextInt(255));
        }
        return new ArrayList<>(integerSet);
    }

    @Test
    void testFirstConstructor(){
        ArrayList<Integer> integerArrayList = generateList();
        Deck<Integer> integerDeck = new Deck<>(integerArrayList);
        Deck<Integer> newDeck = new Deck<>(integerDeck.getDeckState());
        while(integerDeck.hasNext()){
            assertEquals(integerDeck.next(), newDeck.next());
        }
    }

    @Test
    void testEmptyConstructor(){
        Deck<Integer> integerDeck = new Deck<>(new ArrayList<>());
        assertFalse(integerDeck.hasNext());
    }

    @RepeatedTest(20)
    void returnsExactly() {
        ArrayList<Integer> integerArrayList = generateList();
        Deck<Integer> integerDeck = new Deck<>(integerArrayList);
        Set<Integer> integerSetOriginal = new HashSet<>(integerArrayList);
        Set<Integer> picked = new HashSet<>();
        while (integerDeck.hasNext()){
            picked.add(integerDeck.next());
        }
        assertEquals(integerSetOriginal, picked);
    }

    @RepeatedTest(20)
    void takesFromStash() {
        ArrayList<Integer> integerArrayList = generateList();
        Deck<Integer> integerDeck = new Deck<>(new ArrayList<>());
        for (Integer i: integerArrayList) {
            integerDeck.discard(i);
        }

        Set<Integer> integerSetOriginal = new HashSet<>(integerArrayList);
        Set<Integer> picked = new HashSet<>();
        while (integerDeck.hasNext()){
            picked.add(integerDeck.next());
        }
        assertEquals(integerSetOriginal, picked);
    }

    @RepeatedTest(20)
    void checkTakeAll() {
        ArrayList<Integer> integerArrayList = generateList();
        Deck<Integer> integerDeck = new Deck<>(integerArrayList);
        int n = integerArrayList.size();
        Collection<Integer> taken = integerDeck.take(n+1);
        assertEquals(n, taken.size());
    }

    @RepeatedTest(20)
    void checkTakeSubset() {
        ArrayList<Integer> integerArrayList = generateList();
        Deck<Integer> integerDeck = new Deck<>(integerArrayList);
        int n = (new Random()).nextInt(integerArrayList.size());
        Collection<Integer> taken = integerDeck.take(n);
        assertEquals(n, taken.size());
    }


    @Test
    void discard() {
        Deck<Integer> integerDeck = new Deck<>(new ArrayList<>());
        int n = (new Random()).nextInt(100);
        integerDeck.discard(n);
        assertEquals(n, integerDeck.next());
    }

    @Test
    void testException(){
        Deck<Integer> integerDeck = new Deck<>(new ArrayList<>());
        assertThrows(NoSuchElementException.class, integerDeck::next);
    }

    @Test
    void testCardPicked(){
    }
}