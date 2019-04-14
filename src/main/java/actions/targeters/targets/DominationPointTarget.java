package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;

/**
 * This target represents {@link player.DominationPoint} in the sandbox, it is a subclass of
 * {@link BasicTarget}, similarly to {@link player.DominationPoint} being a subclass of
 * {@link player.Pawn}
 *
 * The only significant difference is that Domination Points can't reach any tile other than
 * their own and don't see any tile
 */
public class DominationPointTarget extends BasicTarget {

    DominationPointTarget(DamageableUID target, TileUID initialPosition) {
        super(target, initialPosition);
    }

    DominationPointTarget(Sandbox sandbox, BasicTarget template) {
        super(sandbox, template);
    }

    /**
     * For T.H.O.R. we treat Domination point as a blind pawn
     * @return the empty set
     */
    @Override
    public HashSet<TileUID> tilesSeen() {
        return new HashSet<>();
    }

    /**
     * Because of vortex, tractor beam and others we treat the Domination Point as an unmovable
     * target
     * @param radius is the maximum distance, anything less than 0 should return an empty set,
     * returning just the UIDS.TileUID of the current cell
     * @return empty set if radius less than 0 and a single element set with the location otherwise
     */
    @Override
    public HashSet<TileUID> reachableSelector(int radius) {
        HashSet<TileUID> ret = new HashSet<>();
        if (radius<0) return ret;
        ret.add(location());
        return ret;
    }
}
