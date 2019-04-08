package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;

public class DominationPointTarget extends BasicTarget {

    DominationPointTarget(Sandbox sandbox, DamageableUID target) {
        super(sandbox, target);
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
    public HashSet<TileUID> reachableSelector(int min, int max) {
        HashSet<TileUID> ret = new HashSet<>();
        if (min>=1 || max <0) return ret;
        ret.add(location());
        return ret;
    }
}
