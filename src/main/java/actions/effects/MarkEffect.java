package actions.effects;

import player.Actor;
import uid.DamageableUID;

public class MarkEffect implements Effect{
    private final int amount;
    private final DamageableUID uid;

    public MarkEffect(DamageableUID i, int amount) {
        this.amount = amount;
        this.uid = i;
    }

    @Override
    public EffectType type() {
        return null;
    }

    @Override
    public String effectString(Actor pov) {
        return String.format("%s ha subito %d marchi da %s",
                pov.pawn().getUsername(),
                amount,
                pov.getGm().getPawn(uid).getUsername()
        );
    }
}
