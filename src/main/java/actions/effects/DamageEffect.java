package actions.effects;

import player.Actor;
import testcontroller.SlaveController;
import uid.DamageableUID;

public class DamageEffect implements Effect{
    private final DamageableUID uid;
    private final int amount;
    private final boolean raw;

    //TODO: mirino non converte marchi in punti danno !!

    /**
     *
     * @param uid the damaged actor
     * @param amount the amount of damage
     * @param raw if true use a function which doesn't trigger the tagback grenade
     */
    DamageEffect(DamageableUID uid, int amount, boolean raw){

        this.uid = uid;
        this.amount = amount;
        this.raw = raw;
    }

    @Override
    public EffectType type() {
        return EffectType.DAMAGE;
    }

    /**
     * @param pov
     * @param finalize contains all the instructions to run after the end of the effect. Contains
     *                 also the list of effects still to apply
     */
    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {
        if (raw){
            pov.getSelf().getGm().getPawn(uid).getActor().damageRaw(pov.getSelf(), amount, finalize);
        } else {
            pov.getSelf().getGm().getPawn(uid).getActor().damage(pov.getSelf(), amount, finalize);
        }
    }

    @Override
    public String effectString(Actor pov) {
        return String.format("%s ha dato %d danni a %s", pov.pawn().getUsername(), amount,
                pov.getGm().getPawn(uid).getActor().pawn().getUsername());
    }
}
