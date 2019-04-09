package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import board.Direction;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.*;

public class DirectionTarget extends Targetable implements SuperTile, HavingPointLike {
    List<TileUID> tiles;
    final TileUID source;
    final Direction dir;
    final Sandbox sandbox;

    DirectionTarget(Sandbox sandbox, TileUID origin, Direction direction, boolean logical){
        tiles = new ArrayList<>();
        tiles.add(origin);

        source = origin;
        dir = direction;

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

    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    @Override
    public boolean filteringHas(PointLike target, boolean negation) {
        return negation ^ tiles.contains(target.location());
    }
}
