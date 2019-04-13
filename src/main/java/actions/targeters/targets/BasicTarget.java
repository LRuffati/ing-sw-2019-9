package actions.targeters.targets;

import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.TargetedSelector;
import actions.targeters.interfaces.Visible;
import board.Sandbox;
import player.DominationPoint;
import player.Pawn;
import uid.DamageableUID;
import actions.targeters.interfaces.PointLike;
import uid.TileUID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This target primarily identifies an individual Pawn, be it a player controlled pawn, a terminator, a turret or a
 * domination point.
 *
 */
public class BasicTarget extends Targetable implements PointLike, Visible, TargetedSelector {

    private final Sandbox sandbox;
    private final DamageableUID selfUID;
    private TileUID location;

    BasicTarget(DamageableUID target, TileUID initialPosition){
        selfUID = target;
        sandbox = null;
        location = initialPosition;
    }

    BasicTarget(Sandbox sandbox, BasicTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            selfUID = template.selfUID;
            this.sandbox = sandbox;
        }
    }

    public static BasicTarget basicFactory(Pawn target){
        return new BasicTarget(target.damageableUID, target.getTile());
    }

    public static BasicTarget basicFactory(DominationPoint target){
        return new DominationPointTarget(target.damageableUID, target.getTile());
    }

    public static BasicTarget basicFactory(Sandbox sandbox, BasicTarget template){
        return new BasicTarget(sandbox, template);
    }

    public static BasicTarget basicFactory(Sandbox sandbox, DominationPointTarget template){
        return new DominationPointTarget(sandbox, template);
    }


    /**
     * @return a list of all Pawns (the actual pawns and the domination points) in the current selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    @Override
    public Set<DamageableUID> getSelectedPawns() {
        Collection<DamageableUID> retVal = new ArrayList<>();
        retVal.add(selfUID);
        return new HashSet<>(retVal);
    }

    /**
     * @return a list of all Tiles in or occupied by elements of the Target
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        Collection<TileUID> tiles = new ArrayList<>();
        tiles.add(location());
        return new HashSet<>(tiles);
    }

    /**
     * @return the current location of the PointLike target
     */
    @Override
    public TileUID location() {
        return location;
    }

    /**
     * This is a selector, it generates a list of tiles the current PointLike target can see
     *
     * @return a Collection of TileUIDs without duplicates
     */
    @Override
    public Set<TileUID> tilesSeen() {
        return sandbox.tilesSeen(location());
    }

    @Override
    public Set<TileUID> reachableSelector(int min, int max) {
        return distanceSelector(min, max, true);
    }

    /**
     * for the basictarget (reaches () this ) selector
     * <p>
     * It should first generate a list of basictargets () distant from this
     * with: this.distantSelector( ... , logical=true ).stream.flatMap(sandbox::pawnsInTile).collect(Collectors::toList)
     * and then filter the targets by applying the reachedCondition
     *
     * @param radius
     * @return
     */
    @Override
    public Set<DamageableUID> reachedSelector(int radius) {
        return null;
    }

    @Override
    public Set<DamageableUID> reachedSelector(int min, int max) {
        return null;
    }

    /**
     * The method used for the "distant [from] this" selector
     *
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDs.TileUID of the current cell
     * @param logical if true only consider logical links
     * @return a list of reachable points in the given amount of steps or less
     */
    @Override
    public Set<TileUID> distanceSelector(int radius, boolean logical) {
        return sandbox.circle(location(), radius, logical);
    }

    /**
     * this can be reached by source?
     */
    @Override
    public boolean reachedCondition(int min, int max, PointLike source, boolean negation) {
        Set<TileUID> circle = source.reachableSelector(min, max);
        return negation ^ circle.contains(location());
    }

    /**
     * this is distant from source?
     */
    @Override
    public boolean distanceCondition(int min, int max, PointLike source, boolean negation, boolean logical) {
        Set<TileUID> circle = source.distanceSelector(min, max, logical);
        return negation ^ circle.contains(location());
    }

    /**
     * @param container the SuperTile establishing the condition
     * @param negation  whether the condition should be negated or not
     * @return For targets partially or totally satisfying the condition it returns the sub-target which satisfies it, otherwise empty optional
     */
    @Override
    public boolean containedSelector(SuperTile container, boolean negation) {
        Collection<TileUID> containedTiles = container.containedTiles();
        return negation ^ containedTiles.contains(location());
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
        return negation ^ source.tilesSeen().contains(location());
    }
}
