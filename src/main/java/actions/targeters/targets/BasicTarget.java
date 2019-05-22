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
import viewclasses.TargetView;

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
     * The UID, same as the UID of the mirrored pawn (or domination point)
     */
    private final DamageableUID selfUID;

    // Constructors

    /**
     * This creates a template for the BasicTarget, it's needed because of the bootstrap phase
     * during the map creation
     * @param target the UID of the Pawn I'm cloning
     */
    BasicTarget(@NotNull DamageableUID target){
        selfUID = target;
    }

    /**
     * Creates a new target template from a Pawn
     * @param target the pawn
     * @return the BasicTarget
     */
    @NotNull
    @Contract("_ -> new")
    public static BasicTarget basicFactory(@NotNull Pawn target){
        return new BasicTarget(target.getDamageableUID());
    }

    /**
     * Creates a new target template from a DominationPoint
     * @param target the pawn
     * @return the DominationPointTarget
     */
    @NotNull
    @Contract("_ -> new")
    // TODO: test this, implement visitor pattern if needed
    public static BasicTarget basicFactory(@NotNull DominationPoint target){
        return new DominationPointTarget(target.damageableUID);
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
    public Set<DamageableUID> getSelectedPawns(Sandbox sandbox) {
        Collection<DamageableUID> retVal = new ArrayList<>();
        retVal.add(selfUID);
        return new HashSet<>(retVal);
    }

    /**
     * @return a set with only this Target's location
     */
    @Override
    public Set<TileUID> getSelectedTiles(Sandbox sandbox) {
        Collection<TileUID> tiles = new ArrayList<>();
        tiles.add(sandbox.tile(selfUID));
        return new HashSet<>(tiles);
    }

    /*
     Implementing PointLike
    */

    /**
     * @return the current location of the BasicTarget
     */
    @Override
    public TileUID location(Sandbox sandbox) {
        return sandbox.tile(selfUID);
    }

    /**
     * @return all the cells seen by the BasicTarget
     */
    @Override
    public Set<TileUID> tilesSeen(Sandbox sandbox) {
        if (sandbox == null) throw new AssertionError();
        return sandbox.tilesSeen(sandbox.tile(selfUID));
    }

    /**
     * The method used for the "distant [from] this" selector
     *
     * @param radius is the maximum distance, anything less than 0 should return an empty set, with 0 returning just the UIDs.TileUID of the current cell
     * @param logical if true only consider logical links
     * @return a list of reachable points in the given amount of steps or less
     */
    @Override
    public Set<TileUID> distanceSelector(Sandbox sandbox, int radius, boolean logical) {
        if (sandbox == null) throw new AssertionError();
        return sandbox.circle(sandbox.tile(selfUID), radius, logical);
    }

    /**
     * @param radius the amount of maximum allowed steps
     * @return the set of Pawns which can reach this target in at most radius steps
     */
    @Override
    public Set<DamageableUID> reachedSelector(Sandbox sandbox, int radius) {
        return distanceSelector(sandbox, radius, true).stream() // All the TileUID within range
                .flatMap(i-> sandbox.containedPawns(i).stream()) // All the BasicTargets (UID) in the tileUIDs above
                .filter(i->sandbox.getBasic(i).reachableSelector(sandbox, radius).contains(sandbox.tile(selfUID))) //
                // Only the BasicTargets which can reach "this"
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
    public boolean reachedCondition(Sandbox sandbox, int min, int max,
                                    @NotNull actions.targeters.interfaces.PointLike source, boolean negation) {
        Set<TileUID> circle = source.reachableSelector(sandbox, min, max);
        return negation ^ circle.contains(sandbox.tile(selfUID));
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
    public boolean distanceCondition(Sandbox sandbox, int min, int max, @NotNull PointLike source, boolean negation, boolean logical) {
        Set<TileUID> circle = source.distanceSelector(sandbox, min, max, logical);
        return negation ^ circle.contains(location(sandbox));
    }

    /**
     * @param container the SuperTile establishing the condition
     * @param negation  whether the condition should be negated or not
     * @return negation XOR whether this Pawn is contained in container
     */
    @Override
    public boolean containedSelector(Sandbox sandbox, @NotNull SuperTile container, boolean negation) {
        Collection<TileUID> containedTiles = container.containedTiles(sandbox);
        return negation ^ containedTiles.contains(location(sandbox));
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
    public boolean seen(Sandbox sandbox, PointLike source, boolean negation) {
        return negation ^ source.tilesSeen(sandbox).contains(location(sandbox));
    }

    /**
     * Used for the universal selector
     * @return all the tiles in the same sandbox as the BasicTarget
     */
    public Set<TileUID> coexistingTiles(Sandbox sandbox){
        if (sandbox == null) throw new NullPointerException();
        return sandbox.allTiles();
    }


    @Override
    public TargetView generateView(Sandbox sandbox) {
        return sandbox.generateActorView(selfUID);
    }
}
