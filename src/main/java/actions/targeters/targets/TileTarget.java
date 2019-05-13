package actions.targeters.targets;

import actions.targeters.interfaces.*;
import board.Sandbox;
import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a Tile in the sandbox as well as a tile being targeted
 */
public class TileTarget implements Targetable, PointLike, SuperTile, TargetedSelector, Visible, HavingPointLike {

    /**
     * This is the same TileUID the actual {@link board.Tile} has, allowing all Tile methods to
     * be re used for the TargetTile
     */
    private final TileUID tileUID;

    /**
     * This is a temporary structure needed to deal with the bootstrap phase of the Sandbox,
     * during which TargetTiles and Sandbox both need each other to be created
     * @param id the same UID as the {@link board.Tile}
     * @see board.Tile
     */
    public TileTarget(TileUID id){
        tileUID = id;
    }

    /**
     * @see Targetable#getSelectedPawns(Sandbox)
     * @return the Pawns contained in the tile
     */
    @Override
    public Set<DamageableUID> getSelectedPawns(Sandbox sandbox) {
        return new HashSet<>(sandbox.containedPawns(tileUID));
    }

    /**
     * @see Targetable#getSelectedTiles(Sandbox)
     */
    @Override
    public Set<TileUID> getSelectedTiles(Sandbox sandbox) {
        Set<TileUID> ret = new HashSet<>();
        ret.add(tileUID);
        return ret;
    }

    /**
     * @see HavingPointLike#filteringHas(Sandbox, PointLike, boolean)
     * @param target the pawn or tile I'm checking is in this tile
     * @param negation negate the result
     * @return negation XOR (target.location() == this.location())
     */
    @Override
    public boolean filteringHas(Sandbox sandbox, @NotNull PointLike target, boolean negation) {
        return negation ^ (target.location(sandbox).equals(tileUID));
    }

    /**
     * @see PointLike#location(Sandbox)
     * @return the TileUID of this tile
     */
    @Override
    public TileUID location(Sandbox sandbox) {
        return tileUID;
    }

    /**
     * @see PointLike#tilesSeen(Sandbox)
     * @return All the cells which can be seen from this cell
     */
    @Override
    public Set<TileUID> tilesSeen(Sandbox sandbox) {
        if (sandbox == null) throw new NullPointerException();
        return sandbox.tilesSeen(location(sandbox));
    }

    /**
     * @see PointLike#reachedSelector(Sandbox, int)
     * @param radius the number of moves it takes at most to reach the cells being returned
     * @return a set of cells at most radius moves distant from this
     */
    @Override
    public Set<DamageableUID> reachedSelector(Sandbox sandbox, int radius) {
        if (sandbox == null) throw new NullPointerException();
        return distanceSelector(sandbox, radius, true).stream() // All the TileUID within range
                .flatMap(i-> sandbox.containedPawns(i).stream()) // All the BasicTargets (UID) in the tileUIDs above
                .filter(i->sandbox.getBasic(i).reachableSelector(sandbox, radius).contains(this.location(sandbox))) // Only the BasicTargets which can reach "this"
                .collect(Collectors.toSet());
    }

    /**
     * @see PointLike#distanceSelector(Sandbox, int, boolean)
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @param logical if true don't cross walls
     * @return a list of reachable points in the given amount of steps or less
     */
    @Override
    public Set<TileUID> distanceSelector(Sandbox sandbox, int radius, boolean logical) {
        if (sandbox == null) throw new NullPointerException();
        return sandbox.circle(location(sandbox),radius,logical);
    }

    /**
     * @see SuperTile#containedTiles(Sandbox)
     * @return all the tiles contained in the supertile
     */
    @Override
    public Set<TileUID> containedTiles(Sandbox sandbox) {
        return getSelectedTiles(sandbox);
    }

    /**
     * @see TargetedSelector#reachedCondition(Sandbox, int, int, PointLike, boolean)
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target for which reachableSelector is calculated
     * @param negation whether the condition should be negated or not
     * @return negation XOR this tile can be reached by source in the given range
     */
    @Override
    public boolean reachedCondition(Sandbox sandbox, int min, int max, PointLike source, boolean negation) {
        return distanceCondition(sandbox, min,max,source,negation, true);
    }

    /**
     * @see TargetedSelector#distanceCondition(Sandbox, int, int, PointLike, boolean, boolean)
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @param logical if true don't go through walls
     * @return negation XOR this tile is in the given distance range from source
     */
    @Override
    public boolean distanceCondition(Sandbox sandbox, int min, int max, @NotNull PointLike source, boolean negation, boolean logical) {
        Set<TileUID> circle = source.distanceSelector(sandbox, min, max, logical);
        return negation ^ circle.contains(location(sandbox));
    }

    /**
     * @see TargetedSelector#containedSelector(Sandbox, SuperTile, boolean)
     *
     * @param container the SuperTile establishing the condition
     * @param negation whether the condition should be negated or not
     * @return negation XOR this tile is in the SuperTile container
     */
    @Override
    public boolean containedSelector(Sandbox sandbox, @NotNull SuperTile container, boolean negation) {
        return negation ^ container.containedTiles(sandbox).contains(tileUID);
    }

    /**
     * @see Visible#seen(Sandbox, PointLike, boolean)
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR this tile can be seen by source
     */
    @Override
    public boolean seen(Sandbox sandbox, @NotNull PointLike source, boolean negation) {
        return negation ^ source.tilesSeen(sandbox).contains(location(sandbox));
    }
}
