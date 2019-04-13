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
import java.util.function.Function;
import java.util.stream.Stream;

public class RoomTarget implements Targetable, Visible, HavingPointLike, SuperTile {
    private final Sandbox sandbox;
    private final RoomUID roomid;
    public RoomTarget(RoomUID id){
        roomid = id;
        sandbox = null;
    }

    public RoomTarget(Sandbox sandbox, RoomTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            roomid = template.roomid;
            this.sandbox = sandbox;
        }

    }

    @Override
    public Set<DamageableUID> getSelectedPawns() {
        assert sandbox != null;
        Set<DamageableUID> retVal = new HashSet<>();
        for (TileUID i: sandbox.tilesInRoom(roomid)){
            retVal.addAll(sandbox.containedPawns(i));
        }
        return retVal;
    }

    @Override
    public Set<TileUID> getSelectedTiles() {
        assert sandbox != null;
        return new HashSet<>(sandbox.tilesInRoom(roomid));
    }

    /**
     * @return the sandbox containing the target
     */
    @Override
    public Sandbox getSandbox() {
        return sandbox;
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
