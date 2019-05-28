package viewclasses;

import uid.DamageableUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.Collection;

public class TargetView implements Serializable {
    private String gameMapViewId;
    private Collection<DamageableUID> damageableUIDList;
    private Collection<TileUID> tileUIDList;

    public TargetView(String sandboxUID, Collection<DamageableUID> damageableUIDList, Collection<TileUID> tileUIDList) {
        this.gameMapViewId = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
    }

    public String getGameMapViewId() {
        return gameMapViewId;
    }

    public Collection<DamageableUID> getDamageableUIDList() {
        return damageableUIDList;
    }

    public Collection<TileUID> getTileUIDList() {
        return tileUIDList;
    }
}
