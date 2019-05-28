package actions.effects;

import uid.TileUID;

public class GrabEffect implements Effect {
    private final TileUID cell;
    public final EffectType type;

    public GrabEffect(TileUID cell) {
        this.cell = cell;
        this.type = EffectType.GRAB;
    }

    @Override
    public EffectType type() {
        return EffectType.GRAB;
    }
}
