package grabbables;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;

import java.util.Map;


public class AmmoCard extends Grabbable {

    private final AmmoAmount ammoAmount;
    private final int numOfPowerUp;

    public AmmoCard(AmmoAmount ammoAmount, int numOfPowerUp){
        this.ammoAmount= ammoAmount;
        this.numOfPowerUp = numOfPowerUp;
    }

    public AmmoAmount getAmmoAmount() {
        return ammoAmount;
    }

    public int getNumOfPowerUp() {
        return numOfPowerUp;
    }

    @Override
    public String toString() {
        String ret;
        Map<AmmoColor, Integer> amount = ammoAmount.getAmounts();
        ret = "RED:" + amount.get(AmmoColor.RED)
                +" BLUE:" + amount.get(AmmoColor.BLUE)
                +" YELLOW:" + amount.get(AmmoColor.YELLOW)
                +" POWERUP:"+ numOfPowerUp;
        return ret;
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
