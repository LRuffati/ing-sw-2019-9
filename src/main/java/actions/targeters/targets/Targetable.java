package actions.targeters.targets;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.TargetView;

import java.util.Set;

/**
 * Abstract interface providing the most basic common methods of all Targets
 */
public interface Targetable {
    /**
     * @param sandbox The sandbox to query
     * @return a set of all Pawns (the actual pawns and the domination points) in the current
     * selection, if the selector primarily identifies tiles return all pawns in those tiles
     */
    Set<DamageableUID> getSelectedPawns(Sandbox sandbox);

    /**
     * @param sandbox The sandbox to query
     * @return a set of all Tiles in or occupied by elements of the Target
     */
    Set<TileUID> getSelectedTiles(Sandbox sandbox);

    default boolean equals(Targetable other, Sandbox sandbox){
        Set<DamageableUID> pawnsThis = getSelectedPawns(sandbox);
        Set<DamageableUID> pawnsOther = other.getSelectedPawns(sandbox);

        Set<TileUID> tilesThis = getSelectedTiles(sandbox);
        Set<TileUID> tilesOther = other.getSelectedTiles(sandbox);
        boolean pawns = pawnsThis.size()==pawnsOther.size() && pawnsOther.containsAll(pawnsThis);

        boolean tiles = tilesThis.size()==tilesOther.size() && tilesOther.containsAll(tilesThis);

        return pawns && tiles;
    }

    default boolean isSelf(DamageableUID selfUid){
        return false;
    }

    TargetView generateView(Sandbox sandbox);

    boolean equalsVisitor(Targetable other);

    default boolean matchesBasic(BasicTarget other){
        return false;
    }

    default boolean matchesDirection(DirectionTarget other){
        return false;
    }

    default boolean matchesGroup(GroupTarget other){
        return false;
    }

    default boolean matchesTile(TileTarget other){
        return false;
    }

    default boolean matchesRoom(RoomTarget other){
        return false;
    }

}
