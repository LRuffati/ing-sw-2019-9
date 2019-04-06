package uid;


import java.rmi.server.UID;

public class TileUID {
    private UID tileUID;

    public TileUID(){
        this.tileUID = new UID();
    }

    public boolean equals(TileUID t){
        return super.equals(t);
    }
}
