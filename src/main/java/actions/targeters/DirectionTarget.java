package actions.targeters;

import actions.targeters.interfaces.SuperTile;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;

public class DirectionTarget extends Targetable implements SuperTile {
    @Override
    Collection<DamageableUID> getSelectedPawns() {
        return null;
    }

    @Override
    Collection<TileUID> getSelectedTiles() {
        return null;
    }

    @Override
    public Collection<TileTarget> containedTiles() {
        return null;
    }
}
