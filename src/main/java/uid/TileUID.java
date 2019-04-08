package uid;


import board.Tile;

import java.rmi.server.UID;

public class TileUID {
    private final UID tileUID;

    public TileUID(){
        this.tileUID = new UID();
    }

    public boolean equals(TileUID t){
        return super.equals(t);
    }
}
