package uid;

import java.rmi.server.UID;

public class AmmoTileUID {
    private UID ammoTileUID;

    public AmmoTileUID(){
        this.ammoTileUID = new UID();
    }

    public boolean equals(AmmoTileUID t){
        return ammoTileUID.equals(t.ammoTileUID);
    }
}
