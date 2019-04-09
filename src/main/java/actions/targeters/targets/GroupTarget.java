package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GroupTarget extends Targetable {
    HashSet<DamageableUID> targets;
    Sandbox sandbox;

    GroupTarget(Sandbox sandbox, Collection<DamageableUID> players){
        targets = new HashSet<>(players);
        this.sandbox = sandbox;
    }

    @Override
    public Set<DamageableUID> getSelectedPawns() {
        return new HashSet<>(targets);
    }

    @Override
    public Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        for (DamageableUID i: targets){
            ret.add(sandbox.tile(i));
        }
        return ret;
    }
}
