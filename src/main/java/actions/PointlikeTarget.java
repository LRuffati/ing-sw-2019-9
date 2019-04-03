package actions;

import UIDS.*;

import java.util.*;

public interface PointlikeTarget {
    /**
     * Selector for the "seen [by] this" condition
     * @return a collection of TileUIDs corresponding to visible cells, the cell of the owner included
     */
    Collection<TileUID> seenCells();
    /**
     * @return the UIDS.TileUID of the current cell
     */
    TileUID location();
    /**
     * The method used for the "reached [by] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    HashSet<TileUID> reachable(int radius);

    /**
     * Similar to reachable(radius) but works in a range
     * @param max the maximum distance included
     * @param min the minimum distance included
     * @return the set of cells satisfying the condition
     */
    default HashSet<TileUID> reachable(int min, int max){
        HashSet<TileUID> a = reachable(max);
        a.removeAll(reachable(min - 1));
        return a;
    }

    /**
     * The method used for the "distant [from] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    HashSet<TileUID> distant(int radius);

    /**
     * Similar to distant(radius) but works in a range
     * @param max the maximum distance included
     * @param min the minimum distance included
     * @return the set of cells satisfying the condition
     */
    default HashSet<TileUID> distant(int min, int max){
        HashSet<TileUID> a = distant(max);
        a.removeAll(distant(min-1));
        return a;
    }

    /**
     * The method used in the "has this" selector
     * @return a set consisting of only the current cell
     */
    default HashSet<TileUID> inside(){
        HashSet<TileUID> ret = new HashSet<>();
        ret.add(location());
        return ret;
    }
}
