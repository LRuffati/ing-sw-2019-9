package actions.targeters;

import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.TargetedSelector;
import actions.targeters.interfaces.Visible;
import uid.DamageableUID;
import actions.targeters.interfaces.PointLike;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;

/**
 * This target primarily identifies an individual Pawn, be it a player controlled pawn, a terminator, a turret or a
 * domination point.
 *
 */
public class BasicTarget extends Targetable implements PointLike, Visible, TargetedSelector {

    /**
     * @return a list of all Pawns (the actual pawns and the domination points) in the current selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    @Override
    Collection<DamageableUID> getSelectedPawns() {
        return null;
    }

    /**
     * @return a list of all Tiles in or occupied by elements of the Target
     */
    @Override
    Collection<TileUID> getSelectedTiles() {
        return null;
    }

    /**
     * @return the current location of the PointLike target
     */
    @Override
    public TileUID location() {
        return null;
    }

    /**
     * This is a selector, it generates a list of tiles the current PointLike target can see
     *
     * @return a Collection of TileUIDs without duplicates
     */
    @Override
    public HashSet<TileUID> tilesSeen() {
        return null;
    }

    /**
     * The method used for the "distant [from] this" selector
     *
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @return a list of reachable points in the given amount of steps or less
     */
    @Override
    public HashSet<TileUID> distanceSelector(int radius) {
        return null;
    }

    /**
     * @param min      the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max      the maximum included distance
     * @param source   the PointLike Target for which reachableSelector is calculated
     * @param negation whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    @Override
    public boolean reachedCondition(int min, int max, PointLike source, boolean negation) {
        return false;
    }

    /**
     * @param min      the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max      the maximum included distance
     * @param source   the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    @Override
    public boolean distanceCondition(int min, int max, PointLike source, boolean negation) {
        return false;
    }

    /**
     * @param container the SuperTile establishing the condition
     * @param negation  whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    @Override
    public boolean containedSelector(SuperTile container, boolean negation) {
        return false;
    }

    /**
     * This function filters the targets based on visibility
     *
     * @param source   the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return if the object can be seen or partially seen (or not seen) returns the visible (or not visible) sub-selection, otherwise returns empty optional
     */
    @Override
    public boolean seen(PointLike source, boolean negation) {
        return false;
    }
}
