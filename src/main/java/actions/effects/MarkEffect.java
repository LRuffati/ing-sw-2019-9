package actions.effects;

import player.Actor;
import controller.SlaveController;
import uid.DamageableUID;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Method used to handle the marking of a certain player
 */
public class MarkEffect implements Effect {
    private final int amount;
    private final DamageableUID uid;

    public MarkEffect(DamageableUID i, int amount) {
        this.amount = amount;
        this.uid = i;
    }

    @Override
    public EffectType type() {
        return EffectType.MARK;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {
        broadcaster.accept(effectString(pov.getSelf()));
        pov.getSelf().getGm().getPawn(uid).getActor().addMark(pov.getSelf(), amount);
        finalize.run();
    }

    String effectString(Actor pov) {
        return String.format(
                "%s ha dato %d marchi a %s",
                pov.getGm().getPawn(pov.pawnID()).getUsername(),
                amount,
                pov.getGm().getPawn(uid).getUsername()
        );
    }

    @Override
    public Set<DamageableUID> targetedPlayers() {
        return Set.of(uid);
    }
}
