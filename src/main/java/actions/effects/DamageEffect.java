package actions.effects;

import player.Actor;
import controller.SlaveController;
import uid.DamageableUID;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Deals a certain amount of damage to the actor of the given pawn
 */
public class DamageEffect implements Effect{

    private final DamageableUID uid;
    private final int amount;

    /**
     * If true don't trigger the tagback grenade on application
     */
    private final boolean raw;

    /**
     *
     * @param uid the damaged actor
     * @param amount the amount of damage
     * @param raw if true use a function which doesn't trigger the tagback grenade
     */
    public DamageEffect(DamageableUID uid, int amount, boolean raw){

        this.uid = uid;
        this.amount = amount;
        this.raw = raw;
    }

    @Override
    public EffectType type() {
        if (raw)
            return EffectType.DAMAGERAW;
        return EffectType.DAMAGE;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {
        broadcaster.accept(effectString(pov.getSelf()));
        if (raw){
            pov.getSelf().getGm().getPawn(uid).getActor().damageRaw(pov.getSelf(), amount);
        } else {
            pov.getSelf().getGm().getPawn(uid).getActor().damage(pov.getSelf(), amount);
        }
        new Thread(()->finalize.run()).start();
        return;

    }

    String effectString(Actor pov) {
        return String.format("%s ha dato %d danni a %s", pov.pawn().getUsername(), amount,
                pov.getGm().getPawn(uid).getActor().pawn().getUsername());
    }

    @Override
    public Set<DamageableUID> targetedPlayers() {
        return Set.of(uid);
    }
}
