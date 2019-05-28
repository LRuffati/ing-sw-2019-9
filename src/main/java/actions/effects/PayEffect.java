package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;

public class PayEffect implements Effect{
    private final AmmoAmountUncapped amount;

    public PayEffect(AmmoAmount amount) {
        this.amount = new AmmoAmountUncapped(amount);
    }

    public PayEffect(AmmoAmountUncapped amount) {
        this.amount = amount;
    }

    @Override
    public EffectType type() {
        return EffectType.PAY;
    }

    @Override
    public AmmoAmountUncapped newAmmoAvailable(AmmoAmountUncapped old) {
        return old.subtract(amount);
    }
}
