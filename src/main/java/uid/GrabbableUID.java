package uid;

import java.rmi.server.UID;

public class GrabbableUID {
    private UID grabbableUID;

    public GrabbableUID(){
        this.grabbableUID = new UID();
    }

    public boolean equals(GrabbableUID t){
        return grabbableUID.equals(t.grabbableUID);
    }
}
