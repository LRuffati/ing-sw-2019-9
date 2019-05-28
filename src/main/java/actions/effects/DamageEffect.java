package actions.effects;

import uid.DamageableUID;

public class DamageEffect implements Effect{
    private final DamageableUID uid;
    private final int amount;

    DamageEffect(DamageableUID uid, int amount){

        this.uid = uid;
        this.amount = amount;
    }

    @Override
    public EffectType type() {
        return EffectType.DAMAGE;
    }
}
