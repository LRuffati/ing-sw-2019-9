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
     * The sandbox to which the tile belongs, similar to how {@link board.Tile} contains a
     * reference to {@link board.GameMap}
     */
    private final Sandbox sandbox;

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
        sandbox = null;
    }

    /**
     * This constructor is called by the Sandbox and creates a full TileTarget
     *
     * @param sandbox the sandbox containing the Tile
     * @param template the TileTarget without sandbox reference {@link TileTarget#TileTarget(TileUID)}
     */
    public TileTarget(Sandbox sandbox, TileTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            tileUID = template.tileUID;
            this.sandbox = sandbox;
        }

    }

    /**
     * @see Targetable#getSelectedPawns()
     * @return the Pawns contained in the tile
     */
    @Override
    public Set<DamageableUID> getSelectedPawns() {
        assert sandbox != null;
        return new HashSet<>(sandbox.containedPawns(tileUID));
    }

    /**
     * @see Targetable#getSelectedTiles()
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        ret.add(tileUID);
        return ret;
    }

    /**
     * @see HavingPointLike#filteringHas(PointLike, boolean)
     * @param target the pawn or tile I'm checking is in this tile
     * @param negation negate the result
     * @return negation XOR (target.location() == this.location())
     */
    @Override
    public boolean filteringHas(@NotNull PointLike target, boolean negation) {
        return negation ^ (target.location() == tileUID);
    }

    /**
     * @see PointLike#location()
     * @return the TileUID of this tile
     */
    @Override
    public TileUID location() {
        return tileUID;
    }

    /**
     * @see PointLike#tilesSeen()
     * @return All the cells which can be seen from this cell
     */
    @Override
    public Set<TileUID> tilesSeen() {
        assert sandbox != null;
        return sandbox.tilesSeen(location());
    }

    /**
     * @see PointLike#reachedSelector(int)
     * @param radius the number of moves it takes at most to reach the cells being returned
     * @return a set of cells at most radius moves distant from this
     */
    @Override
    public Set<DamageableUID> reachedSelector(int radius) {
        assert sandbox != null;
        return distanceSelector(radius, true).stream() // All the TileUID within range
                .flatMap(i-> sandbox.containedPawns(i).stream()) // All the BasicTargets (UID) in the tileUIDs above
                .filter(i->sandbox.getBasic(i).reachableSelector(radius).contains(this.location())) // Only the BasicTargets which can reach "this"
                .collect(Collectors.toSet());
    }

    /**
     * @see PointLike#distanceSelector(int, boolean)
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDS.TileUID of the current cell
     * @param logical if true don't cross walls
     * @return a list of reachable points in the given amount of steps or less
     */
    @Override
    public Set<TileUID> distanceSelector(int radius, boolean logical) {
        assert sandbox != null;
        return sandbox.circle(location(),radius,logical);
    }

    /**
     * @see SuperTile#containedTiles()
     * @return all the tiles contained in the supertile
     */
    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    /**
     * @see TargetedSelector#reachedCondition(int, int, PointLike, boolean)
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target for which reachableSelector is calculated
     * @param negation whether the condition should be negated or not
     * @return negation XOR this tile can be reached by source in the given range
     */
    @Override
    public boolean reachedCondition(int min, int max, PointLike source, boolean negation) {
        return distanceCondition(min,max,source,negation, true);
    }

    /**
     * @see TargetedSelector#distanceCondition(int, int, PointLike, boolean, boolean)
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @param logical if true don't go through walls
     * @return negation XOR this tile is in the given distance range from source
     */
    @Override
    public boolean distanceCondition(int min, int max, @NotNull PointLike source, boolean negation, boolean logical) {
        Set<TileUID> circle = source.distanceSelector(min, max, logical);
        return negation ^ circle.contains(location());
    }

    /**
     * @see TargetedSelector#containedSelector(SuperTile, boolean)
     *
     * @param container the SuperTile establishing the condition
     * @param negation whether the condition should be negated or not
     * @return negation XOR this tile is in the SuperTile container
     */
    @Override
    public boolean containedSelector(@NotNull SuperTile container, boolean negation) {
        return negation ^ container.containedTiles().contains(tileUID);
    }

    /**
     * @see Visible#seen(PointLike, boolean)
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR this tile can be seen by source
     */
    @Override
    public boolean seen(@NotNull PointLike source, boolean negation) {
        return negation ^ source.tilesSeen().contains(location());
    }
}
