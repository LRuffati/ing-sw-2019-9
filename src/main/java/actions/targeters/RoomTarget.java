package actions.targeters;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.Visible;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;

public class RoomTarget extends Targetable implements Visible, HavingPointLike, SuperTile {
    @Override
    Collection<DamageableUID> getSelectedPawns() {
        return null;
    }

    @Override
    Collection<TileUID> getSelectedTiles() {
        return null;
    }

    @Override
    public boolean filteringHas(PointLike target) {
        return false;
    }

    @Override
    public Collection<HavingPointLike> selectingHas(PointLike target) {
        return null;
    }

    @Override
    public Collection<TileTarget> containedTiles() {
        return null;
    }

    @Override
    public boolean seen(PointLike source, boolean negation) {
        return false;
    }
}
