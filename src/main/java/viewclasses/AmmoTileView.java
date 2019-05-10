package viewclasses;

import java.io.Serializable;

/**
 * This class contains the AmmoTile card that is used by the view and transmitted from the server to the client
 */
public class AmmoTileView implements Serializable {
    private int numOfRed;
    private int numOfBlue;
    private int numOfYellow;
    private int numOfPowerUp;

    public AmmoTileView(){
        numOfBlue = 0;
        numOfRed = 0;
        numOfYellow = 0;
        numOfPowerUp = 0;
    }

    public void setNumOfBlue(int numOfBlue) {
        this.numOfBlue = numOfBlue;
    }

    public void setNumOfPowerUp(int numOfPowerUp) {
        this.numOfPowerUp = numOfPowerUp;
    }

    public void setNumOfRed(int numOfRed) {
        this.numOfRed = numOfRed;
    }

    public void setNumOfYellow(int numOfYellow) {
        this.numOfYellow = numOfYellow;
    }

    public int numOfBlue() {
        return numOfBlue;
    }

    public int numOfPowerUp() {
        return numOfPowerUp;
    }

    public int numOfRed() {
        return numOfRed;
    }

    public int numOfYellow() {
        return numOfYellow;
    }
}
