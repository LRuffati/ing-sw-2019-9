package actions.targeters.interfaces;

import uid.TileUID;

import java.util.Set;

/**
 * This class implements the selector:
 * PointLike (in Supertile)
 */
public interface  SuperTile {
    /**
     * @return
     */
    Set<TileUID> containedTiles();
}
