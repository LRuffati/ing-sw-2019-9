package actions.targeters.targets;

import uid.DamageableUID;
import uid.TileUID;

import java.util.Set;

/**
 * Abstract interface providing the most basic common methods of all Targets
 */
public abstract class Targetable {
    /**
     *
     * @return a list of all Pawns (the actual pawns and the domination points) in the current selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    abstract Set<DamageableUID> getSelectedPawns();

    /**
     *
     * @return a list of all Tiles in or occupied by elements of the Target
     */
    abstract Set<TileUID> getSelectedTiles();
}
