package uid;


import board.Tile;
import org.jetbrains.annotations.NotNull;

import java.rmi.server.UID;

public class TileUID {
    private final UID tileUID;

    public TileUID(){
        this.tileUID = new UID();
    }

    public Boolean equals(@NotNull TileUID t){
        return tileUID.equals(t.tileUID);
    }
}
