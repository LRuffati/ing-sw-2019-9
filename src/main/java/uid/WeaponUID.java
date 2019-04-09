package uid;
import java.rmi.server.UID;

public class WeaponUID {
    private UID weaponUID;

    public WeaponUID(){
        this.weaponUID = new UID();
    }

    public boolean equals(WeaponUID t){
        return weaponUID.equals(t.weaponUID);
    }
}
