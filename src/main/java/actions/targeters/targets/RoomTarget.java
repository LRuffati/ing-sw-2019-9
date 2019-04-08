package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.Visible;
import board.Sandbox;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

public class RoomTarget extends Targetable implements Visible, HavingPointLike, SuperTile {
    private final Sandbox sandbox;
    private final RoomUID roomid;
    RoomTarget(Sandbox sandbox, RoomUID id){
        this.sandbox = sandbox;
        roomid = id;
    }

    @Override
    Set<DamageableUID> getSelectedPawns() {
        Set<DamageableUID> retVal = new HashSet<>();
        for (TileUID i: sandbox.tilesInRoom(roomid)){
            retVal.addAll(sandbox.containedPawns(i));
        }
        return retVal;
    }

    @Override
    Set<TileUID> getSelectedTiles() {
        return new HashSet<>(sandbox.tilesInRoom(roomid));
    }

    @Override
    public boolean filteringHas(PointLike target, boolean negation) {
        return negation ^ getSelectedTiles().contains(target.location());
    }

    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    @Override
    public boolean seen(PointLike source, boolean negation) {
        Set<TileUID> seenTiles = source.tilesSeen();
        return negation ^ getSelectedTiles().parallelStream().anyMatch(seenTiles::contains);
    }
}
