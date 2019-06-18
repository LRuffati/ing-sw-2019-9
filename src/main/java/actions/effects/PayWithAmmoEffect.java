package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import player.Actor;
import testcontroller.SlaveController;

public class PayWithAmmoEffect implements Effect {
    private final AmmoAmountUncapped amountToPay;

    public PayWithAmmoEffect(AmmoAmountUncapped amountToPay) {
        this.amountToPay = amountToPay;
    }

    @Override
    public EffectType type() {
        return EffectType.PAY;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {
        pov.getSelf().pay(new AmmoAmount(amountToPay));
        finalize.run();
    }

    public AmmoAmount newAmmoAvailable(AmmoAmount old){
        return new AmmoAmount(old.subtract(amountToPay));
    }

    @Override
    public String effectString(Actor pov) {
        return String.format("%s paga %s usando cubi", pov.pawn().getUsername(),
                amountToPay.toString());
    }
}
