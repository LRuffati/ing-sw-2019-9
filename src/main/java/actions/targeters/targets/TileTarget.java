package actions.targeters.targets;

import actions.targeters.interfaces.*;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TileTarget implements Targetable, PointLike, SuperTile, TargetedSelector, Visible, HavingPointLike {

    private final Sandbox sandbox;
    private final TileUID tileUID;

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
    public Set<DamageableUID> getSelectedPawns() {
        assert sandbox != null;
        return new HashSet<>(sandbox.containedPawns(tileUID));
    }

    @Override
    public Set<TileUID> getSelectedTiles() {
        Set<TileUID> ret = new HashSet<>();
        ret.add(tileUID);
        return ret;
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
        return negation ^ (target.location() == tileUID);
    }

    @Override
    public TileUID location() {
        return tileUID;
    }

    @Override
    public Set<TileUID> tilesSeen() {
        assert sandbox != null;
        return sandbox.tilesSeen(location());
    }

    /**
     * for the basictarget (reaches () this ) selector
     * <p>
     * It should first generate a list of basictargets () distant from this
     * with: this.distantSelector( ... , logical=true ).stream.flatMap(sandbox::pawnsInTile).collect(Collectors::toList)
     * and then filter the targets by applying the reachedCondition
     *
     * @return the set of PawnTargets which have this.location() in their reachableSelector(radius)
     */

    @Override
    public Set<DamageableUID> reachedSelector(int radius) {
        assert sandbox != null;
        return distanceSelector(radius, true).stream() // All the TileUID within range
                .flatMap(i-> sandbox.containedPawns(i).stream()) // All the BasicTargets (UID) in the tileUIDs above
                .filter(i->sandbox.getBasic(i).reachableSelector(radius).contains(this.location())) // Only the BasicTargets which can reach "this"
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DamageableUID> reachedSelector(int min, int max) {
        HashSet<DamageableUID> ret = new HashSet<>(reachedSelector(max)); //All the BasicTargets which can reach this point in <= max
        ret.removeAll(reachedSelector(min-1)); // remove all BasicTargets which can reach this point in < min
        return ret;
    }

    @Override
    public Set<TileUID> distanceSelector(int radius, boolean logical) {
        assert sandbox != null;
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
        Set<TileUID> circle = source.distanceSelector(min, max, logical);
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
