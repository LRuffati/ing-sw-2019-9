package viewclasses;

import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TargetView implements Serializable {
    private String gameMapViewId;
    private Collection<DamageableUID> damageableUIDList;
    private Collection<TileUID> tileUIDList;

    public TargetView(String sandboxUID, @NotNull Collection<DamageableUID> damageableUIDList,
                      @NotNull Collection<TileUID> tileUIDList) {
        this.gameMapViewId = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
    }

    public String getGameMapViewId() {
        return gameMapViewId;
    }

    public Set<DamageableUID> getDamageableUIDList() {
        return new HashSet<>(damageableUIDList);
    }

    public Set<TileUID> getTileUIDList() {
        return new HashSet<>(tileUIDList);
    }
}
