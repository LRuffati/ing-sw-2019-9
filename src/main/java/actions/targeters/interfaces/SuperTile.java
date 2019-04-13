package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import uid.TileUID;

import java.util.Set;

/**
 * This class implements the selector:
 * PointLike (in Supertile)
 */
public interface  SuperTile extends Targetable {
    /**
     * @return
     */
    Set<TileUID> containedTiles();
}
