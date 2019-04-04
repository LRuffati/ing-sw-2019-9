package actions.targeters.interfaces;

import uid.TileUID;

import java.util.HashSet;

public interface PointLike {
    /**
     *
     * @return the current location of the PointLike target
     */
    TileUID location();

    /**
     * This is a selector, it generates a list of tiles the current PointLike target can see
     * @return a Collection of TileUIDs without duplicates
     */
    HashSet<TileUID> tilesSeen();

    /**
     * The method used for the "reached [by] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    default HashSet<TileUID> reachable(int radius){
        return distant(radius);
    }

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
}
