package actions.targeters.targets;

import actions.targeters.interfaces.*;
import board.Sandbox;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TileTarget extends Targetable implements PointLike, SuperTile, TargetedSelector, Visible, HavingPointLike {

    Sandbox sandbox;
    TileUID tileUID;

    public TileTarget(TileUID id){
        tileUID = id;
        sandbox = null;
    }

    public TileTarget(Sandbox sandbox, TileTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            tileUID = template.tileUID;
            this.sandbox = sandbox;
        }

    }


    @Override
    Set<DamageableUID> getSelectedPawns() {
        return new HashSet<>(sandbox.containedPawns(tileUID));
    }

    @Override
    Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        ret.add(tileUID);
        return ret;
    }

    @Override
    public boolean filteringHas(PointLike target, boolean negation) {
        return negation ^ (target.location() == tileUID);
    }

    @Override
    public TileUID location() {
        return tileUID;
    }

    @Override
    public Set<TileUID> tilesSeen() {
        return sandbox.tilesSeen(location());
    }

    /**
     * for the basictarget (reaches () this ) selector
     * <p>
     * It should first generate a list of basictargets () distant from this
     * with: this.distantSelector( ... , logical=true ).stream.flatMap(sandbox::pawnsInTile).collect(Collectors::toList)
     * and then filter the targets by applying the reachedCondition
     *
     * @param radius
     * @return
     */
    @Override
    public HashSet<DamageableUID> reachedSelector(int radius) {
        return null;
    }

    @Override
    public HashSet<DamageableUID> reachedSelector(int min, int max) {
        return null;
    }

    @Override
    public HashSet<TileUID> distanceSelector(int radius, boolean logical) {
        return sandbox.circle(location(),radius,logical);
    }

    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    @Override
    public boolean reachedCondition(int min, int max, PointLike source, boolean negation) {
        return distanceCondition(min,max,source,negation, true);
    }

    @Override
    public boolean distanceCondition(int min, int max, PointLike source, boolean negation, boolean logical) {
        HashSet<TileUID> circle = source.distanceSelector(min, max, logical);
        return negation ^ circle.contains(location());
    }

    @Override
    public boolean containedSelector(SuperTile container, boolean negation) {
        return negation ^ container.containedTiles().contains(tileUID);
    }

    @Override
    public boolean seen(PointLike source, boolean negation) {
        return negation ^ source.tilesSeen().contains(location());
    }
}
