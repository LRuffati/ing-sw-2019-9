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

    public AmmoCard(AmmoAmount ammoAmount, int numOfPowerUp){
        this.ammoAmount= ammoAmount;
        this.numOfPowerUp = numOfPowerUp;
    }

    /**
     * @return The amount of ammo contained in the Card
     */
    @Override
    public AmmoAmount getAmmoAmount() {
        return ammoAmount;
    }

    /**
     * @return The number of PowerUps available in the card
     */
    @Override
    public int getNumOfPowerUp() {
        return numOfPowerUp;
    }


    public AmmoCardView generateView(){
        AmmoCardView ammoCardView = new AmmoCardView();
        ammoCardView.setNumOfBlue(ammoAmount.getAmounts().get(AmmoColor.BLUE));
        ammoCardView.setNumOfRed(ammoAmount.getAmounts().get(AmmoColor.RED));
        ammoCardView.setNumOfYellow(ammoAmount.getAmounts().get(AmmoColor.YELLOW));
        ammoCardView.setNumOfPowerUp(numOfPowerUp);

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

    @Override
    public Set<AmmoCard> getCard() {
        return Set.of(this);
    }
}
