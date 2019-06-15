package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import actions.utils.AmmoColor;
import player.Actor;

public class PayEffect implements Effect{
    private final AmmoAmountUncapped amount;

    public PayEffect(AmmoAmount amount) {
        this.amount = new AmmoAmount(amount);
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


    @Override
    public String effectString(Actor pov) {
        return String.format("%s ha usato %d munizioni rosse, %d munizioni gialle, %d munizioni blu",
                pov.pawn().getUsername(),
                amount.getAmounts().get(AmmoColor.RED),
                amount.getAmounts().get(AmmoColor.YELLOW),
                amount.getAmounts().get(AmmoColor.BLUE)
                );
    }
}
