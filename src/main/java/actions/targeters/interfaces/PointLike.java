package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

public interface PointLike extends Targetable {
    /**
     *
     * @return the current location of the PointLike target
     */
    TileUID location(Sandbox sandbox);

    /**
     * This is a selector, it generates a list of tiles the current PointLike target can see
     * @return a Collection of TileUIDs without duplicates
     */
    Set<TileUID> tilesSeen(Sandbox sandbox);

    /**
     * The method used for the "reached [by] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set,
     * returning just the UIDS.TileUID of the current cell
     * @return a list of reachableSelector points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    default Set<TileUID> reachableSelector(Sandbox sandbox, int radius){
        return distanceSelector(sandbox, radius, true);
    }

    /**
     * Similar to reachableSelector(radius) but works in a range
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @return the set of cells satisfying the condition
     */
    default Set<TileUID> reachableSelector(Sandbox sandbox, int min, int max){
        Set<TileUID> a = reachableSelector(sandbox, max);
        a.removeAll(reachableSelector(sandbox, min - 1));
        return a;
    }

    /**
     * for the basictarget (reaches () this ) selector
     *
     * This function is semantically different from distanceSelector, it is used to determine a
     * distance when selecting a target which will need to move to this cell
     *
     * For domination points it makes a difference
     *
     * @param radius the amount of maximum allowed steps
     * @return the set of all cells able to reach this target in at most radius steps
     */
    Set<DamageableUID> reachedSelector(Sandbox sandbox, int radius);

    /**
     * @see PointLike#reachedSelector(Sandbox, int)
     * @param min the minimum (included) number of steps
     * @param max the maximum (included) number of steps
     * @return the cells reachable in at least min and at most max steps
     */
    default Set<DamageableUID> reachedSelector(Sandbox sandbox, int min, int max){
        HashSet<DamageableUID> ret = new HashSet<>(reachedSelector(sandbox, max)); //All the
        // BasicTargets which can reach this point in <= max
        ret.removeAll(reachedSelector(sandbox, min-1)); // remove all BasicTargets which can reach
        // this point in < min
        return ret;
    }

    /**
     * The method used for the "distant [from] this" selector
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @param logical if true don't cross walls
     * @return a list of reachable points in the given amount of steps or less
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    Set<TileUID> distanceSelector(Sandbox sandbox, int radius, boolean logical);

    /**
     * Similar to distanceSelector(radius) but works in a range
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @param logical if true don't cross walls
     * @return the set of cells satisfying the condition
     */
    default Set<TileUID> distanceSelector(Sandbox sandbox, int min, int max, boolean logical){
        Set<TileUID> a = distanceSelector(sandbox, max,logical);
        a.removeAll(distanceSelector(sandbox,min-1, logical));
        return a;
    }
}
