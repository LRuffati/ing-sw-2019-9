package actions.targeters;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashSet;
import java.util.Set;

public class StubTarget implements Targetable {
    private final Sandbox sandbox;

    /**
     * @return a list of all Pawns (the actual pawns and the domination points) in the current selection, if the selector primarily identifies tiles return all pawns in those tiles
     */

    StubTarget(Sandbox sandbox){
        this.sandbox = sandbox;
    }

    @Override
    public Set<DamageableUID> getSelectedPawns() {
        return new HashSet<>();
    }

    /**
     * @return a list of all Tiles in or occupied by elements of the Target
     */
    @Override
    public Set<TileUID> getSelectedTiles() {
        return new HashSet<>();
    }

}
