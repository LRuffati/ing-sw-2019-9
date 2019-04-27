package grabbables;

import actions.utils.AmmoAmount;

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
    public AmmoAmount getAmmoAmount() {
        return ammoAmount;
    }

    /**
     * @return The number of PowerUps available in the card
     */
    public int getNumOfPowerUp() {
        return numOfPowerUp;
    }

    @Override
    public String toString() {
        return ammoAmount.toString() +" POWERUP:"+ numOfPowerUp;
    }

    //TODO Check if the overrides are needed.
    /*
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    */

}
