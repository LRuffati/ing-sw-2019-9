package uid;
import java.rmi.server.UID;

public class RoomUID {
    private UID roomUID;

    public RoomUID(){
        this.roomUID = new UID();
    }

    public boolean equals(RoomUID t){
        return super.equals(t);
    }
}
