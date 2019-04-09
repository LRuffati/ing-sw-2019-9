package actions.targeters.factories;

import board.Sandbox;
import uid.DamageableUID;
import uid.TileUID;

import java.util.Collection;

public class SelectorOutput {
    protected final Collection<TileUID> tiles;
    protected final Collection<DamageableUID> pawns;
    protected final Sandbox sandbox;

    public SelectorOutput(Collection<TileUID> tiles, Collection<DamageableUID> pawns, Sandbox sandbox){
        this.tiles = tiles;
        this.pawns = pawns;
        this.sandbox = sandbox;
    }
}
