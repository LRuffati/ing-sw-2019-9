package grabbables;
import genericitems.Tuple3;

import java.util.*;

/**
 * This class implements a deck of shuffled elements where already used elements can be added to the deck
 * in case more requests are made than there are cards in the deck
 * @param <T> is an iterable
 */
// TODO: find a way to avoid a caller modifying the content of the T elements when they are supposed to be in the deck
public class Deck<T> implements Iterator<T>{
    private Iterator<T> deckIterator;
    private Collection<T> stash;
    private Set<T> pickedCard;

    /**
     * This method creates a Deck class from a saved state, preserving the original ordering of the deckIterator
     * @param savedState the Tuple used to store information previously generated by getDeckState
     */
    protected Deck(Tuple3<List<T>, Set<T>, Set<T>> savedState){
        deckIterator = savedState.x.listIterator();
        stash = savedState.y;
        pickedCard = savedState.z;
    }

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
        pickedCard = new HashSet<>();
    }

    /**
     * @return  The element on top of the deck, if the deckIterator is empty it empties the stash
     *          by creating a new deckIterator with its contents
     */
    public T next() {
        T elem;
        if (deckIterator.hasNext()) {
            elem = deckIterator.next();
            pickedCard.add(elem);
            return elem;
        }

        if (stash.isEmpty()){
            throw new NoSuchElementException("Nothing in stash");
        }

        ArrayList<T> temp = new ArrayList<>(stash);
	    Collections.shuffle(temp);
        deckIterator = temp.iterator();
        stash.clear();
        elem = deckIterator.next();
        pickedCard.add(elem);
        return elem;
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
        pickedCard.remove(card);
        stash.add(card);
    }


    /**
     * This methods checks if the card is in the pickedCard set.
     * Typically a card can be used only if it is not in the Stash or in the Deck
     * @param card The card that is requested to check
     * @return True if the card is not in the Deck or in the Stash
     */
    public boolean isPicked(T card){
        return pickedCard.contains(card);
    }

    /**
     * This method turns the information contained in the Deck into a tuple for storage.
     * Calling the method destroys the Deck class, and if the class were still needed it'd have to be constructed using the appropriate constructor
     *
     * @return A tuple where the first element is a List of the cards in the iterator, in the order they'd have been uncovered, the second element is an unordered set of the cards stashed for future use
     */
    public Tuple3<List<T>, Set<T>, Set<T>> getDeckState(){

        List<T> list = new ArrayList<>();
        deckIterator.forEachRemaining(list::add);
        return new Tuple3<>(list, new HashSet<>(stash), new HashSet<>(pickedCard));
    }
}
