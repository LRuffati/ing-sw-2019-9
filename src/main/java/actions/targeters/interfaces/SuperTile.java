package actions.targeters.interfaces;

import actions.targeters.TileTarget;

import java.util.Collection;

/**
 * This class implements the selector:
 * PointLike (in Supertile)
 */
public interface  SuperTile {
    /**
     *
     * @return
     */
    Collection<TileTarget> containedTiles();
}
