package viewclasses;

import board.Sandbox;
import uid.DamageableUID;
import uid.SandboxUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.Collection;

public class TargetView implements Serializable {
    private SandboxUID uid;
    private Collection<DamageableUID> damageableUIDList;
    private Collection<TileUID> tileUIDList;

    public TargetView(SandboxUID sandboxUID, Collection<DamageableUID> damageableUIDList, Collection<TileUID> tileUIDList) {
        this.uid = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
    }

    public SandboxUID getUid() {
        return uid;
    }

    public Collection<DamageableUID> getDamageableUIDList() {
        return damageableUIDList;
    }

    public Collection<TileUID> getTileUIDList() {
        return tileUIDList;
    }

    //TODO: this method
    public GameMapView apply(Sandbox sandbox){
        return sandbox.generateView();
    }
}
