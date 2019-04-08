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
     * Checks the visibility of a target
     * @param target the target for which I'm checking visibility
     * @return true if the target is visible, false otherwise
     */
    default boolean sees(PointLike target){
        return tilesSeen().contains(target.location());
    }

    /**
     * The method used for the "reached [by] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, returning just the UIDS.TileUID of the current cell
     * @return a list of reachableSelector points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    default HashSet<TileUID> reachableSelector(int radius){
        return distanceSelector(radius);
    }

    /**
     * Similar to reachableSelector(radius) but works in a range
     * @param max the maximum distance included
     * @param min the minimum distance included
     * @return the set of cells satisfying the condition
     */
    default HashSet<TileUID> reachableSelector(int min, int max){
        HashSet<TileUID> a = reachableSelector(max);
        a.removeAll(reachableSelector(min - 1));
        return a;
    }

    /**
     * The method used for the "distant [from] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    HashSet<TileUID> distanceSelector(int radius);

    /**
     * Similar to distanceSelector(radius) but works in a range
     * @param max the maximum distance included
     * @param min the minimum distance included
     * @return the set of cells satisfying the condition
     */
    default HashSet<TileUID> distanceSelector(int min, int max){
        HashSet<TileUID> a = distanceSelector(max);
        a.removeAll(distanceSelector(min-1));
        return a;
    }
}
