package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.TargetView;

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

    public GroupTarget(Collection<DamageableUID> players){
        targets = new HashSet<>(players);
    }

    /**
     * {@link Targetable#getSelectedPawns(Sandbox)}
     */
    @Override
    public Set<DamageableUID> getSelectedPawns(Sandbox sandbox) {
        return new HashSet<>(targets);
    }

    /**
     * {@link Targetable#getSelectedTiles(Sandbox)}
     */
    @Override
    public Set<TileUID> getSelectedTiles(Sandbox sandbox) {
        Set<TileUID> ret = new HashSet<>();
        for (DamageableUID i: targets){
            ret.add(sandbox.tile(i));
        }
        return ret;
    }


    @Override
    public TargetView generateView(Sandbox sandbox) {
        return sandbox.generateDamageableListView(targets);
    }
}
