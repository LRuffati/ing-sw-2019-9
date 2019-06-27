package viewclasses;

import uid.GrabbableUID;

import java.io.Serializable;

/**
 * This class contains the AmmoTile card that is used by the view and transmitted from the server to the client
 */
public class AmmoCardView implements Serializable {
    private int numOfRed;
    private int numOfBlue;
    private int numOfYellow;
    private int numOfPowerUp;
    private GrabbableUID uid;

    public AmmoCardView(){
        numOfBlue = 0;
        numOfRed = 0;
        numOfYellow = 0;
        numOfPowerUp = 0;
    }

    public void setAll(int numOfBlue, int numOfRed, int numOfYellow, int numOfPowerUp) {
        this.numOfBlue = numOfBlue;
        this.numOfRed = numOfRed;
        this.numOfYellow = numOfYellow;
        this.numOfPowerUp = numOfPowerUp;
    }

    public void setUid(GrabbableUID uid) {
        this.uid = uid;
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

    public GrabbableUID uid() {
        return uid;
    }
}
