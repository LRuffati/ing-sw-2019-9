package board;
import uid.TileUID;

import java.util.*;

/** Used to represent a Tile that is physically near to another.
 * A boolean is used to check whether the tile is reachable.
 */
public class NeightTile {

    /**
     * Constructs the object
     * @param tile TileUID of the tile
     * @param reachable True if tile can be directly reached
     */
    public NeightTile(TileUID tile, Boolean reachable) {
        this.tile = tile;
        this.reachable = reachable;
    }

    private TileUID tile;
    private Boolean reachable;

    /**
     * @return An Optional containing the tile if is reachable, Optional.empty otherwise
     */
    protected Optional<TileUID> logical() {
        return reachable ? Optional.of(tile) : Optional.empty();
    }

    /**
     * @return The tile placed near the caller position
     */
    protected TileUID physical() {
        return tile;
    }

}
