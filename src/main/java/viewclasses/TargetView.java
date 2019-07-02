package viewclasses;

import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the information of {@link actions.targeters.targets.Targetable Targets} needed by the client.
 * It only contains getters and setters
 */
public class TargetView implements Serializable {
    private String gameMapViewId;
    private List<DamageableUID> damageableUIDList;
    private List<TileUID> tileUIDList;
    private boolean dedicatedColor;

    public TargetView(String sandboxUID, @NotNull List<DamageableUID> damageableUIDList,
                      @NotNull List<TileUID> tileUIDList, boolean dedicatedColor) {
        this.gameMapViewId = sandboxUID;
        this.damageableUIDList = damageableUIDList;
        this.tileUIDList = tileUIDList;
        this.dedicatedColor = dedicatedColor;
    }

    public String getGameMapViewId() {
        return gameMapViewId;
    }

    public List<DamageableUID> getDamageableUIDList() {
        return new ArrayList<>(damageableUIDList);
    }

    public List<TileUID> getTileUIDList() {
        return new ArrayList<>(tileUIDList);
    }

    public boolean isDedicatedColor() {
        return dedicatedColor;
    }
}
