package uid;
import org.jetbrains.annotations.NotNull;

import java.rmi.server.UID;

public class AmmoTileUID {
    private UID ammoTileUID;

    public AmmoTileUID(){
        this.ammoTileUID = new UID();
    }

    public boolean equals(@NotNull AmmoTileUID t){
        return ammoTileUID.equals(t.ammoTileUID);
    }
}
