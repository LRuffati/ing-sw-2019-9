package actions.targeters;

import uid.TileUID;

import java.util.HashSet;

public class DominationPointTarget extends BasicTarget {
    @Override
    public HashSet<TileUID> tilesSeen() {
        return new HashSet<>();
    }

    @Override
    //Todo: test if this ripples as intended with the overloaded (range-like) method
    public HashSet<TileUID> reachableSelector(int radius) {
        return new HashSet<>();
    }
}
