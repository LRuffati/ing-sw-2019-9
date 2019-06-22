package actions.effects;

import player.Actor;
import testcontroller.SlaveController;
import uid.DamageableUID;
import uid.TileUID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MoveEffect implements Effect {

    private final DamageableUID pawn;
    private final TileUID dest;

    public MoveEffect(DamageableUID pawn, TileUID dest){

        this.pawn = pawn;
        this.dest = dest;
    }

    @Override
    public EffectType type() {
        return EffectType.MOVE;
    }

    /**
     * Sandbox updating
     *
     * @param old
     * @return
     */
    @Override
    public Map<DamageableUID, TileUID> newLocations(Map<DamageableUID, TileUID> old) {
        Map<DamageableUID, TileUID> newM = new HashMap<>(old);
        newM.put(pawn,dest);
        return newM;
    }

    /**
     * @param pov
     * @param finalize contains all the instructions to run after the end of the effect. Contains
     */
    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {
        pov.getSelf().getGm().getPawn(pawn).move(dest);
        broadcaster.accept(effectString(pov.getSelf()));
        finalize.run();
    }

    String effectString(Actor pov) {
        String message;
        if (pov.pawnID().equals(this.pawn))
            message = String.format("%s si è spostato", pov.pawn().getUsername());
        else
            message = String.format("%s ha spostato %s", pov.pawn().getUsername(),
                    pov.getGm().getPawn(pawn).getUsername());
        return message;
    }
}