package actions.effects;

import actions.utils.AmmoAmount;
import board.Tile;
import grabbables.Grabbable;
import grabbables.Weapon;
import testcontroller.SlaveController;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    @Override
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
