package grabbables;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import viewclasses.AmmoCardView;

import java.util.Set;

/**
 * Card containing an AmmoCard
 */
public class AmmoCard extends Grabbable {
    /**
     * The Ammo available in the card
     */
    private final AmmoAmount ammoAmount;
    /**
     * The number of PowerUps available in the card.
     * Standard cards always contains up to 1 PowerUp
     */
    private final int numOfPowerUp;

    /**
     * Constructor of the class.
     * Given an {@link AmmoAmount} and the number of powerUps it generates the AmmoCard
     */
    public AmmoCard(AmmoAmount ammoAmount, int numOfPowerUp){
        this.ammoAmount= ammoAmount;
        this.numOfPowerUp = numOfPowerUp;
    }

    /**
     * Return The amount of ammo contained in the Card
     * @return The amount of ammo contained in the Card
     */
    @Override
    public AmmoAmount getAmmoAmount() {
        return ammoAmount;
    }

    /**
     * Return The number of PowerUps available in the card
     * @return The number of PowerUps available in the card
     */
    @Override
    public int getNumOfPowerUp() {
        return numOfPowerUp;
    }


    /**
     * Generates a new {@link viewclasses.AmmoCardView ammoCardView} of this ammoCard, containing all the information needed by the client
     * @return a view of the ammoCard
     */
    public AmmoCardView generateView(){
        AmmoCardView ammoCardView = new AmmoCardView();
        ammoCardView.setAll(
                ammoAmount.getAmounts().get(AmmoColor.BLUE),
                ammoAmount.getAmounts().get(AmmoColor.RED),
                ammoAmount.getAmounts().get(AmmoColor.YELLOW),
                numOfPowerUp
        );
        ammoCardView.setUid(super.getId());
        return ammoCardView;
    }


    @Override
    public String toString() {
        return ammoAmount.toString() +" POWERUP:"+ numOfPowerUp;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Return a set containing only this card
     * @return A set containing only this card
     */
    @Override
    public Set<AmmoCard> getCard() {
        return Set.of(this);
    }
}
