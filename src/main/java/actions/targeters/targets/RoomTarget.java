package actions.targeters.targets;

import actions.targeters.interfaces.HavingPointLike;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.SuperTile;
import actions.targeters.interfaces.Visible;
import board.Sandbox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

/**
 * This target represents a Room in the sandbox as well as targeting all players in a room
 */
public class RoomTarget implements Targetable, Visible, HavingPointLike, SuperTile {

    /**
     * The sandbox connected to the target
     */
    private final Sandbox sandbox;

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
        sandbox = null;
    }

    /**
     * Binds the RoomTarget to the sandbox
     * @see TileTarget#TileTarget(Sandbox, TileTarget)
     * @param sandbox the sandbox which will bind to the Target
     * @param template the RoomTarget without sandbox
     */
    public RoomTarget(Sandbox sandbox, RoomTarget template){
        if (template.sandbox != null) throw new IllegalStateException("A sandbox already exists");
        else {
            roomid = template.roomid;
            this.sandbox = sandbox;
        }

    }

    /**
     * Returns the UIDs of all the BasicTargets in the cells contained in the room
     * See {@link Targetable#getSelectedPawns()}
     * @return the pawns in tiles in the room
     */
    @Override
    public Set<DamageableUID> getSelectedPawns() {
        assert sandbox != null;
        Set<DamageableUID> retVal = new HashSet<>();
        for (TileUID i: sandbox.tilesInRoom(roomid)){
            retVal.addAll(sandbox.containedPawns(i));
        }
        return retVal;
    }

    /**
     * The UIDs of the cells contained in the room
     * @see Targetable#getSelectedTiles()
     * @return the tilesUID of the room
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        assert sandbox != null;
        return new HashSet<>(sandbox.tilesInRoom(roomid));
    }


    /**
     * See {@link HavingPointLike#filteringHas(PointLike, boolean)}
     *
     * @param target the target which I'm checking is in the HavingPointLike object
     * @param negation if I'm looking for a positive or negative check
     * @return negation XOR whether the target is on this cell
     */
    @Override
    public boolean filteringHas(@NotNull PointLike target, boolean negation) {
        return negation ^ getSelectedTiles().contains(target.location());
    }

    /**
     * See {@link SuperTile#containedTiles()}
     * @return a set with only this Tile's UID
     */
    @Override
    public Set<TileUID> containedTiles() {
        return getSelectedTiles();
    }

    /**
     * See {@link Visible#seen(PointLike, boolean)}
     *
     * @param source the observer, which has method sees
     * @param negation whether this is a positive or negative condition
     * @return negation XOR this tile can be seen by source
     */
    @Override
    public boolean seen(@NotNull PointLike source, boolean negation) {
        Set<TileUID> seenTiles = source.tilesSeen();
        return negation ^ getSelectedTiles().parallelStream().anyMatch(seenTiles::contains);
    }

}
