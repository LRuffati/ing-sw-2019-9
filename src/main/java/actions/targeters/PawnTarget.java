package actions.targeters;

import UIDS.PawnUID;
import UIDS.TileUID;
import actions.targeters.interfaces.PointLike;
import actions.targeters.interfaces.Visible;

import java.util.Collection;
import java.util.HashSet;

/**
 * This target primarily identifies an individual Pawn, be it a player controlled pawn, a terminator, a turret or a domination point
 *
 */
public class PawnTarget extends Targetable implements PointLike {
    @Override
    public TileUID location() {
        return null;
    }

    @Override
    public HashSet<TileUID> tilesSeen() {
        return null;
    }

    @Override
    public HashSet<TileUID> distant(int radius) {
        return null;
    }

    @Override
    Collection<PawnUID> getSelectedPawns() {
        return null;
    }

    @Override
    Collection<TileUID> getSelectedTiles() {
        return null;
    }
}
