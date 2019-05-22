package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import board.Direction;
import board.Sandbox;
import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.TargetView;

import java.util.*;

public class DirectionTarget implements Targetable, SuperTile, HavingPointLike {
    /**
     * The tiles UIDs contained in the direction
     */
    private List<TileUID> tiles;

    /**
     *
     * @param sandbox the sandbox I'm simulating in
     * @param origin the first cell
     * @param direction the direction to follow
     * @param logical if true stop at the first wall
     */
    public DirectionTarget(Sandbox sandbox, TileUID origin, Direction direction, boolean logical){
        tiles = new ArrayList<>();
        tiles.add(origin);

        Map<Direction, TileUID> next = sandbox.neighbors(origin, logical);
        while (next.containsKey(direction)){
            tiles.add(next.get(direction));
            next = sandbox.neighbors(next.get(direction),logical);
        }
    }

    /**
     *
     * @return all the pawns in the direction
     */
    @Override
    public Set<DamageableUID> getSelectedPawns(Sandbox sandbox) {
        Set<DamageableUID> allPawns = new HashSet<>();
        for (TileUID i: tiles){
            allPawns.addAll(sandbox.containedPawns(i));
        }
        return allPawns;
    }

    /**
     *
     * @return a set of cells on a cardinal direction starting from a given cell
     */
    @Override
    public Set<TileUID> getSelectedTiles(Sandbox sandbox) {
        return new HashSet<>(tiles);
    }

    /**
     *
     * @return same as {@link DirectionTarget#getSelectedTiles(Sandbox)}
     */
    @Override
    public Set<TileUID> containedTiles(Sandbox sandbox) {
        return getSelectedTiles(sandbox);
    }

    /**
     * @param target the target which I'm checking is in the HavingPointLike object
     * @param negation if I'm looking for a positive or negative check
     * @return negation XOR target is in the direction
     */
    @Override
    public boolean filteringHas(Sandbox sandbox, @NotNull PointLike target, boolean negation) {
        return negation ^ tiles.contains(target.location(sandbox));
    }


    @Override
    public TargetView generateView(Sandbox sandbox) {
        return sandbox.generateTileListView(tiles);
    }
}
