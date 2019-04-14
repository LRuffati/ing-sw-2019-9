package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a group of pawns, unlike other selectors the Group target doesn't have
 * its own selectors or filters, instead it is built by taking a Set of {@link BasicTarget} and
 * feeding it into the class constructor
 */
public class GroupTarget implements Targetable {
    /**
     * The UIDs of the targets
     */
    private HashSet<DamageableUID> targets;

    /**
     * The sandbox bound to the GroupTarget
     */
    private final Sandbox sandbox;

    public GroupTarget(Sandbox sandbox, Collection<DamageableUID> players){
        targets = new HashSet<>(players);
        this.sandbox = sandbox;
    }

    /**
     * {@link Targetable#getSelectedPawns()}
     */
    @Override
    public Set<DamageableUID> getSelectedPawns() {
        return new HashSet<>(targets);
    }

    /**
     * {@link Targetable#getSelectedTiles()}
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        for (DamageableUID i: targets){
            ret.add(sandbox.tile(i));
        }
        return ret;
    }

}
