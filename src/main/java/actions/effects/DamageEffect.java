package actions.effects;

import uid.DamageableUID;

public class DamageEffect implements Effect{
    private final DamageableUID uid;
    private final int amount;

    //TODO: mirino non converte marchi in punti danno !!
    DamageEffect(DamageableUID uid, int amount){

        this.uid = uid;
        this.amount = amount;
    }

    @Override
    public EffectType type() {
        return EffectType.DAMAGE;
    }
}
