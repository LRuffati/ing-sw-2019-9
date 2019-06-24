package viewclasses;

import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.List;


public class TargetView implements Serializable {
    private String gameMapViewId;
    private List<DamageableUID> damageableUIDList;
    private List<TileUID> tileUIDList;

    public TargetView(String sandboxUID, @NotNull List<DamageableUID> damageableUIDList,
                      @NotNull List<TileUID> tileUIDList) {
        this.gameMapViewId = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
    }

    public String getGameMapViewId() {
        return gameMapViewId;
    }

    public List<DamageableUID> getDamageableUIDList() {
        return damageableUIDList;
    }

    public List<TileUID> getTileUIDList() {
        return tileUIDList;
    }
}
