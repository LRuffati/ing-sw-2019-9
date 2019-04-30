package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Abstract interface providing the most basic common methods of all Targets
 */
public interface Targetable {
    /**
     * @param sandbox The sandbox to query
     * @return a set of all Pawns (the actual pawns and the domination points) in the current
     * selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    Set<DamageableUID> getSelectedPawns(Sandbox sandbox);

    /**
     * @param sandbox The sandbox to query
     * @return a set of all Tiles in or occupied by elements of the Target
     */
    Set<TileUID> getSelectedTiles(Sandbox sandbox);
}
