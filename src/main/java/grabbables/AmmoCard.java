package grabbables;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import viewclasses.AmmoTileView;

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


    public AmmoTileView generateView(){
        AmmoTileView ammoTileView = new AmmoTileView();
        ammoTileView.setNumOfBlue(ammoAmount.getAmounts().get(AmmoColor.BLUE));
        ammoTileView.setNumOfRed(ammoAmount.getAmounts().get(AmmoColor.RED));
        ammoTileView.setNumOfYellow(ammoAmount.getAmounts().get(AmmoColor.YELLOW));
        ammoTileView.setNumOfPowerUp(numOfPowerUp);

        ammoTileView.setUid(super.getId());
        return ammoTileView;
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
}
