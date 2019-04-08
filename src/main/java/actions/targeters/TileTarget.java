package actions.targeters;

import actions.targeters.interfaces.*;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;

public class TileTarget extends Targetable implements PointLike, SuperTile, TargetedSelector, Visible, HavingPointLike {

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
    public TileUID location() {
        return null;
    }

    @Override
    public HashSet<TileUID> tilesSeen() {
        return null;
    }

    @Override
    public HashSet<TileUID> distanceSelector(int radius) {
        return null;
    }

    @Override
    public Collection<TileTarget> containedTiles() {
        return null;
    }

    @Override
    public boolean reachedCondition(int min, int max, PointLike source, boolean negation) {
        return false;
    }

    @Override
    public boolean distanceCondition(int min, int max, PointLike source, boolean negation) {
        return false;
    }

    @Override
    public boolean containedSelector(SuperTile container, boolean negation) {
        return false;
    }

    @Override
    public boolean seen(PointLike source, boolean negation) {
        return false;
    }
}
