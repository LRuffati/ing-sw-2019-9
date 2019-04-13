package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import board.Direction;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class DirectionTarget implements Targetable, SuperTile, HavingPointLike {
    private List<TileUID> tiles;
    private final Sandbox sandbox;

    public DirectionTarget(Sandbox sandbox, TileUID origin, Direction direction, boolean logical){
        tiles = new ArrayList<>();
        tiles.add(origin);

        Map<Direction, TileUID> next = sandbox.neighbors(origin, logical);
        while (next.containsKey(direction)){
            tiles.add(next.get(direction));
            next = sandbox.neighbors(next.get(direction),logical);
        }

        this.sandbox = sandbox;
    }

    @Override
    public Set<DamageableUID> getSelectedPawns() {
        Set<DamageableUID> allPawns = new HashSet<>();
        for (TileUID i: tiles){
            allPawns.addAll(sandbox.containedPawns(i));
        }
        return allPawns;
    }

    @Override
    public Set<TileUID> getSelectedTiles() {
        return new HashSet<>(tiles);
    }

    /**
     * @return the sandbox containing the target
     */
    @Override
    public Sandbox getSandbox() {
        return sandbox;
    }

    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    @Override
    public boolean filteringHas(PointLike target, boolean negation) {
        return negation ^ tiles.contains(target.location());
    }
}
