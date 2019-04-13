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
     *
     * @return a list of all Pawns (the actual pawns and the domination points) in the current selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    Set<DamageableUID> getSelectedPawns();

    /**
     *
     * @return a list of all Tiles in or occupied by elements of the Target
     */
    Set<TileUID> getSelectedTiles();

    /**
     *
     * @return the sandbox containing the target
     */
    Sandbox getSandbox();
}
