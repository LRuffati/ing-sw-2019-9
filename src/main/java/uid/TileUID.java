package uid;

import java.rmi.server.UID;

public class TileUID {
    private final UID tileUID;

    public TileUID(){
        this.tileUID = new UID();
    }

    public Boolean equals(TileUID t){
        return tileUID.equals(t.tileUID);
    }
}
