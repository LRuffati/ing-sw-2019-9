package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

public class DominationPointTarget extends BasicTarget {

    DominationPointTarget(DamageableUID target, TileUID initialPosition) {
        super(target, initialPosition);
    }

    DominationPointTarget(Sandbox sandbox, BasicTarget template) {
        super(sandbox, template);
    }

    @Override
    public HashSet<TileUID> tilesSeen() {
        return new HashSet<>();
    }

    @Override
    public HashSet<TileUID> reachableSelector(int radius) {
        HashSet<TileUID> ret = new HashSet<>();
        if (radius<0) return ret;
        ret.add(location());
        return ret;
    }

    @Override
    public Set<TileUID> reachableSelector(int min, int max) {
        HashSet<TileUID> ret = new HashSet<>();
        if (min>=1 || max <0) return ret;
        ret.add(location());
        return ret;
    }
}
