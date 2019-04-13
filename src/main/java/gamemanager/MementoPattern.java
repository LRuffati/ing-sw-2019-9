package gamemanager;


import grabbables.Deck;
import genericitems.Tuple;

import java.util.Collection;
import java.util.Set;

/**
 * This class implements the Memento Pattern to save and load a game.
 * @param <T>
 */

// TODO Develop the class with the needed objects to save and load.
// TODO Test the class.
public class MementoPattern<T> {

    private Collection<T> deckComp;
    private Tuple<Collection<T>, Set<T>> deckTuple;


    public MementoPattern(){
        Deck d = new Deck(deckComp);
        this.deckTuple = d.getDeckState();
    }

    public static class Memento<T>{
        private final Collection<T> deckComp;
        private final Tuple<Collection<T>, Set<T>> deckTuple;

        public Memento(Collection<T> compToSave, Tuple<Collection<T>, Set<T>> deckToSave){

            deckComp = compToSave;
            deckTuple = deckToSave;
        }

        private Tuple<Collection<T>, Set<T>> getSavedDeck() {
            return deckTuple;
        }
    }
}

