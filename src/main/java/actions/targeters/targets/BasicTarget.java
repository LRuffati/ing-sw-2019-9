package actions.targeters.targets;

import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.TargetedSelector;
import actions.targeters.interfaces.Visible;
import board.Sandbox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import player.DominationPoint;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This target primarily identifies an individual Pawn, be it a player controlled pawn, a terminator, a turret or a
 * domination point.
 *
 */
public class BasicTarget implements Targetable, PointLike, Visible, TargetedSelector {

    /**
     * The sandbox I'm simulating in
     */
    private final Sandbox sandbox;

    /**
     * The UID, same as the UID of the mirrored pawn (or domination point)
     */
    private final DamageableUID selfUID;

    // Constructors

    /**
     * This creates a template for the BasicTarget, it's needed because of the bootstrap phase
     * during the map creation
     * @param target the UID of the Pawn I'm cloning
     * @param initialPosition the initial position
     */
    BasicTarget(@NotNull DamageableUID target, @NotNull TileUID initialPosition){
        selfUID = target;
        sandbox = null;
    }

    /**
     * Binds a BasicTarget template to a Sandbox
     * @param sandbox the sandbox
     * @param template the template obtained using the other constructor
     */
    BasicTarget(@NotNull Sandbox sandbox, @NotNull BasicTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            selfUID = template.selfUID;
            this.sandbox = sandbox;
        }
    }

    /**
     * Creates a new target template from a Pawn
     * @param target the pawn
     * @return the BasicTarget
     */
    @NotNull
    @Contract("_ -> new")
    public static BasicTarget basicFactory(@NotNull Pawn target){
        return new BasicTarget(target.getDamageableUID(), target.getTile());
    }

    /**
     * Creates a new target template from a DominationPoint
     * @param target the pawn
     * @return the DominationPointTarget
     */
    @NotNull
    @Contract("_ -> new")
    public static BasicTarget basicFactory(@NotNull DominationPoint target){
        return new DominationPointTarget(target.damageableUID, target.getTile());
    }

    /**
     * Binds the template to a sandbox
     *
     * @param sandbox the sandbox
     * @param template the BasicTarget without sandbox
     * @return the bound BasicTarget
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static BasicTarget basicFactory(@NotNull Sandbox sandbox, @NotNull BasicTarget template){
        return new BasicTarget(sandbox, template);
    }

    /**
     * Binds the template to a sandbox
     *
     * @param sandbox the sandbox
     * @param template the DominationPointTarget without sandbox
     * @return the bound DominationPointTarget
     */
    @NotNull
    @Contract("_, _ -> new")
    public static BasicTarget basicFactory(Sandbox sandbox, DominationPointTarget template){
        return new DominationPointTarget(sandbox, template);
    }

    /*
     Implementing Targetable
    */

    /**
     * a list of all Pawns (the actual pawns and the domination points) in the current selection,
     * if the selector primarily identifies tiles return all pawns in those tiles
     * @return a set containing only this DamageableUID
     */
    @Override
    public Set<DamageableUID> getSelectedPawns() {
        Collection<DamageableUID> retVal = new ArrayList<>();
        retVal.add(selfUID);
        return new HashSet<>(retVal);
    }

    /**
     * @return a set with only this Target's location
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        Collection<TileUID> tiles = new ArrayList<>();
        tiles.add(location());
        return new HashSet<>(tiles);
    }

    /*
     Implementing PointLike
    */

    /**
     * @return the current location of the BasicTarget
     */
    @Override
    public TileUID location() {
        return sandbox.tile(selfUID);
    }

    /**
     * @return all the cells seen by the BasicTarget
     */
    @Override
    public Set<TileUID> tilesSeen() {
        assert sandbox != null;
        return sandbox.tilesSeen(location());
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
        assert sandbox != null;
        return sandbox.circle(location(), radius, logical);
    }

    /**
     * @param radius the amount of maximum allowed steps
     * @return the set of Pawns which can reach this target in at most radius steps
     */
    @Override
    public Set<DamageableUID> reachedSelector(int radius) {
        return distanceSelector(radius, true).stream() // All the TileUID within range
                .flatMap(i-> sandbox.containedPawns(i).stream()) // All the BasicTargets (UID) in the tileUIDs above
                .filter(i->sandbox.getBasic(i).reachableSelector(radius).contains(this.location())) // Only the BasicTargets which can reach "this"
                .collect(Collectors.toSet());
    }

    /*
    Implements TargetedSelector
    */

    /**
     * this can be reached by source?
     *
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target for which reachableSelector is calculated
     * @param negation whether the condition should be negated or not
     * @return negation XOR whether this can be reached by the source in the given number of steps
     */
    @Override
    public boolean reachedCondition(int min, int max, @NotNull actions.targeters.interfaces.PointLike source, boolean negation) {
        Set<TileUID> circle = source.reachableSelector(min, max);
        return negation ^ circle.contains(location());
    }

    /**
     * this is distant from source?
     *
     * @param min the minimum included distance (0 includes the current tile, negative values return an empty set
     * @param max the maximum included distance
     * @param source the PointLike Target from which the distance is calculated
     * @param negation whether the condition should be negated or not
     * @param logical whether to go through walls
     * @return negation XOR whether this is in the distance range from source
     */
    @Override
    public boolean distanceCondition(int min, int max, @NotNull PointLike source, boolean negation, boolean logical) {
        Set<TileUID> circle = source.distanceSelector(min, max, logical);
        return negation ^ circle.contains(location());
    }

    /**
     * @param container the SuperTile establishing the condition
     * @param negation  whether the condition should be negated or not
     * @return negation XOR whether this Pawn is contained in container
     */
    @Override
    public boolean containedSelector(@NotNull SuperTile container, boolean negation) {
        Collection<TileUID> containedTiles = container.containedTiles();
        return negation ^ containedTiles.contains(location());
    }

    /*
    Implements Visible
    */

    /**
     * This function filters the targets based on visibility
     *
     * @param source   the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR whether this Pawn can be seen by the source
     */
    @Override
    public boolean seen(actions.targeters.interfaces.PointLike source, boolean negation) {
        return negation ^ source.tilesSeen().contains(location());
    }

    /**
     * Used for the universal selector
     * @return all the tiles in the same sandbox as the BasicTarget
     */
    public Set<TileUID> coexistingTiles(){
        assert sandbox != null;
        return sandbox.allTiles();
    }
}
