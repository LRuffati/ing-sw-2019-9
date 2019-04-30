package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.TileUID;

import java.util.Set;

/**
 * This class implements the selector:
 * PointLike (in Supertile)
 */
public interface  SuperTile extends Targetable {
    /**
     * @return all the tiles contained in the supertile
     */
    Set<TileUID> containedTiles(Sandbox sandbox);
}
