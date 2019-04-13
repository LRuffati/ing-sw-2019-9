package actions.targeters.interfaces;

import uid.DamageableUID;
import uid.TileUID;

import java.util.Set;

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
    Set<TileUID> tilesSeen();

    /**
     * The method used for the "reached [by] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, returning just the UIDS.TileUID of the current cell
     * @return a list of reachableSelector points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    default Set<TileUID> reachableSelector(int radius){
        return distanceSelector(radius, true);
    }

    /**
     * Similar to reachableSelector(radius) but works in a range
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @return the set of cells satisfying the condition
     */
    default Set<TileUID> reachableSelector(int min, int max){
        Set<TileUID> a = reachableSelector(max);
        a.removeAll(reachableSelector(min - 1));
        return a;
    }

    /**
     * for the basictarget (reaches () this ) selector
     *
     * It should first generate a list of basictargets () distant from this
     *      with: this.distantSelector( ... , logical=true ).stream.flatMap(sandbox::pawnsInTile).collect(Collectors::toList)
     *      and then filter the targets by applying the reachedCondition
     *
     * @param radius
     * @return
     */
    Set<DamageableUID> reachedSelector(int radius);

    Set<DamageableUID> reachedSelector(int min, int max);

    /**
     * The method used for the "distant [from] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    Set<TileUID> distanceSelector(int radius, boolean logical);

    /**
     * Similar to distanceSelector(radius) but works in a range
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @return the set of cells satisfying the condition
     */
    default Set<TileUID> distanceSelector(int min, int max, boolean logical){
        Set<TileUID> a = distanceSelector(max,logical);
        a.removeAll(distanceSelector(min-1, logical));
        return a;
    }
}
