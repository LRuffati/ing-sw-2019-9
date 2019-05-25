package viewclasses;

import uid.DamageableUID;
import uid.SandboxUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.List;

public class TargetView implements Serializable {
    private SandboxUID uid;
    private List<DamageableUID> damageableUIDList;
    private List<TileUID> tileUIDList;

    public TargetView(SandboxUID sandboxUID, List<DamageableUID> damageableUIDList, List<TileUID> tileUIDList) {
        this.uid = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
    }

    public SandboxUID getUid() {
        return uid;
    }

    public List<DamageableUID> getDamageableUIDList() {
        return damageableUIDList;
    }

    public List<TileUID> getTileUIDList() {
        return tileUIDList;
    }
}
