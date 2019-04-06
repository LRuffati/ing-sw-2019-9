package uid;
import java.rmi.server.UID;

public class PowerUpUID {
    private UID powerUpUID;

    public PowerUpUID(){
        this.powerUpUID = new UID();
    }

    public boolean equals(PowerUpUID t){
        return super.equals(t);
    }
}
