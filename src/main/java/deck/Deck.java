import java.lang.reflect.Array;
import java.util.*;

/**
 * This class implements a deck of shuffled elements where already used elements can be added to the deck
 * in case more requests are made than there are cards in the deck
 * @param <T> is an iterable
 */
// TODO: find a way to avoid a caller modifying the content of the T elements when they are supposed to be in the deck
// TODO: write tests to check that
public class Deck<T extends Iterable<T>> implements Iterator<T>{
    /*
     *
     */
    private Iterator<T> deckIterator;
    private Collection<T> stash;

    /**
     * The constructor takes a collection of elements it will have to randomize and creates an Iterator
     * from which an element will be taken as needed and a stash in which discarded elements will be kept
     * until the iterator empties
     *
     * @param originalDeck will contain all the elements the deck will have to provide, the elements passed
     *                     to the constructor should not be used further before they have been returned by
     *                     the iterator
     */
    public Deck(Collection<T> originalDeck){
        stash = new ArrayList<>(originalDeck.size());
        ArrayList<T> temp = new ArrayList<>(originalDeck);
        Collections.shuffle(temp);
        deckIterator = temp.iterator();
    }

    /**
     * @return  The element on top of the deck, if the deckIterator is empty it empties the stash
     *          by creating a new deckIterator with its contents
     */
    public T next() {
        if (deckIterator.hasNext()) {
            return deckIterator.next();
        }

        if (stash.isEmpty()){
            throw new NoSuchElementException("Nothing in stash");
        }

        ArrayList<T> temp = new ArrayList<>(stash);
        // TODO: Check what happens when stash has less than maxN elements
	Collections.shuffle(temp);
        deckIterator = temp.iterator();
        stash.clear();
        return deckIterator.next();
    }

    /**
     * Safely take  one or more elements at once, this does not guarantee exactly maxN elements will be returned
     * @param maxN the maximum amount requested by the caller
     *
     * @return an ArrayList containing a number of elements equal to the lowest between maxN and the number
     * of cards in the deckIterator and stash combined
     */
    public Collection<T> take(int maxN){
        ArrayList<T> ret = new ArrayList<>(maxN);
        for (int i=0; (i<maxN) && hasNext(); i++){
            ret.add(next());
        }
        return ret;
    }

    /**
     * @return True if there is at least an element in either the stash or the deckIterator
     */
    public boolean hasNext(){
	// TODO: test with an empty stash if 
        if (!stash.isEmpty()){
            return true;
        }
        return deckIterator.hasNext();
    }

    /**
     * This method grows the stash, in which cards are stored after having been discarded
     * until the deck has been emptied, at which point the stash is converted into a randomized iterator
     *
     * @param card  the card which has been used and needs to be discarded, the card should not be accessed
     *              by any other class after it has been passed as a parameter to discard and until the point
     *              it is taken from the deck by calling next
     */
    public void discard(T card){
        stash.add(card);
    }
}
