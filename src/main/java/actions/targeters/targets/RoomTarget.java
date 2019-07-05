package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.Visible;
import board.Sandbox;
import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;
import viewclasses.TargetView;

import java.util.HashSet;
import java.util.Set;

/**
 * This target represents a Room in the sandbox as well as targeting all players in a room
 */
public class RoomTarget implements Targetable, Visible, HavingPointLike, SuperTile {

    /**
     * The UID of the room, this is the same as the UID of the original room in the actual map
     */
    private final RoomUID roomid;

    /**
     * Constructor for bootstrapping, see {@link TileTarget#TileTarget(TileUID)}
     * @param id the UID of the actual room
     */
    public RoomTarget(RoomUID id){
        roomid = id;
    }

    /**
     * Returns the UIDs of all the BasicTargets in the cells contained in the room
     * See {@link Targetable#getSelectedPawns(Sandbox)}
     * @return the pawns in tiles in the room
     */
    @Override
    public Set<DamageableUID> getSelectedPawns(Sandbox sandbox) {
        if (sandbox == null) throw new NullPointerException();
        Set<DamageableUID> retVal = new HashSet<>();
        for (TileUID i: sandbox.tilesInRoom(roomid)){
            retVal.addAll(sandbox.containedPawns(i));
        }
        return retVal;
    }

    /**
     * The UIDs of the cells contained in the room
     * @see Targetable#getSelectedTiles(Sandbox)
     * @return the tilesUID of the room
     */
    @Override
    public Set<TileUID> getSelectedTiles(Sandbox sandbox) {
        if (sandbox == null) throw new NullPointerException();
        return new HashSet<>(sandbox.tilesInRoom(roomid));
    }


    /**
     * See {@link HavingPointLike#filteringHas(Sandbox, PointLike, boolean)}
     *
     * @param target the target which I'm checking is in the HavingPointLike object
     * @param negation if I'm looking for a positive or negative check
     * @return negation XOR whether the target is on this cell
     */
    @Override
    public boolean filteringHas(Sandbox sandbox, @NotNull PointLike target, boolean negation) {
        return negation ^ getSelectedTiles(sandbox).contains(target.location(sandbox));
    }

    /**
     * See {@link SuperTile#containedTiles(Sandbox)}
     * @return a set with only this Tile's UID
     */
    @Override
    public Set<TileUID> containedTiles(Sandbox sandbox) {
        return getSelectedTiles(sandbox);
    }

    /**
     * See {@link Visible#seen(Sandbox, PointLike, boolean)}
     *
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR this tile can be seen by source
     */
    @Override
    public boolean seen(Sandbox sandbox, @NotNull PointLike source, boolean negation) {
        Set<TileUID> seenTiles = source.tilesSeen(sandbox);
        return negation ^ getSelectedTiles(sandbox).parallelStream().anyMatch(seenTiles::contains);
    }


    @Override
    public TargetView generateView(Sandbox sandbox) {
        return sandbox.generateTargetView(containedTiles(sandbox), true);
    }

    @Override
    public boolean equalsVisitor(Targetable other) {
        return other.matchesRoom(this);
    }

    @Override
    public boolean matchesRoom(RoomTarget other) {
        return this.roomid.equals(other.roomid);
    }
}
