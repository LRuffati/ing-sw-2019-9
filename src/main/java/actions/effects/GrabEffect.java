package actions.effects;


import player.Actor;
import testcontroller.SlaveController;
import uid.TileUID;


import java.util.function.Consumer;


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

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {

    }



    public String effectString(Actor pov) {
        return String.format("%s ha raccolto %s",
                pov.pawn().getUsername(),
                pov.getGm().getTile(pov.getGm().tile(pov.pawnID())).spawnPoint()
                        //todo: come capire che arma ha raccolto?
                        ? "un'arma"
                        : "delle munizioni"
        );
    }
}
