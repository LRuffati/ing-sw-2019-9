package actions.targeters.interfaces;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

public interface PointLike extends Targetable {
    /**
     * @param sandbox the sandbox on which to evaluate the function
     * @return the current location of the PointLike target
     */
    TileUID location(Sandbox sandbox);

    /**
     * This is a selector, it generates a list of tiles the current PointLike target can see
     * @param sandbox the sandbox on which to evaluate the function
     * @return a Collection of TileUIDs without duplicates
     */
    Set<TileUID> tilesSeen(Sandbox sandbox);

    /**
     * The method used for the "reached [by] this" selector
     * @param sandbox the sandbox on which to evaluate the function
     * @param radius is the maximum distance, anything less than 0 should return an empty set, 0
     *               should return a set with just {@link #location(Sandbox)}
     * @return a set of cell reachable in radius moves
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    default Set<TileUID> reachableSelector(Sandbox sandbox, int radius){
        return distanceSelector(sandbox, radius, true);
    }

    /**
     * Similar to reachableSelector(radius) but works in a range
     * @param sandbox the sandbox on which to evaluate the function
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @return the set of cells that can be reached by the target
     */
    default Set<TileUID> reachableSelector(Sandbox sandbox, int min, int max){
        Set<TileUID> a = reachableSelector(sandbox, max);
        a.removeAll(reachableSelector(sandbox, min - 1));
        return a;
    }

    /**
     * The method used for the "distant [from] this" selector
     * @param sandbox the sandbox on which to evaluate the function
     * @param radius is the maximum distance, anything less than 0 should return an empty set, 0
     *               should return a set with just {@link #location(Sandbox)}
     * @param logical if true don't cross walls
     * @return a set of points distant an amount of steps less than or equal to radius
     */
    //Todo: test con radius <=0 and verify that it returns location() and only location()
    Set<TileUID> distanceSelector(Sandbox sandbox, int radius, boolean logical);

    /**
     * Similar to distanceSelector(radius) but works in a range
     * @param sandbox the sandbox on which to evaluate the function
     * @param min the minimum distance included
     * @param max the maximum distance included
     * @param logical if true don't cross walls
     * @return a set of tiles distant an amount of steps contained between min and max (inclusive)
     */
    default Set<TileUID> distanceSelector(Sandbox sandbox, int min, int max, boolean logical){
        Set<TileUID> a = distanceSelector(sandbox, max,logical);
        a.removeAll(distanceSelector(sandbox,min-1, logical));
        return a;
    }
}
