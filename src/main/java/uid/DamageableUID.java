package uid;

import java.rmi.server.UID;

public class DamageableUID {
    private UID damageableUID;

    public DamageableUID(){
        this.damageableUID = new UID();
    }

    public boolean equals(DamageableUID t){
        return super.equals(t);
    }
}
