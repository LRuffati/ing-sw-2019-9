package actions.effects;

import actions.utils.AmmoAmount;
import actions.utils.AmmoAmountUncapped;
import player.Actor;
import controller.SlaveController;

import java.util.function.Consumer;

/**
 * Pays a given amount by decreasing the amount of ammo available to the player
 */
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
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {
        broadcaster.accept(effectString(pov.getSelf()));
        pov.getSelf().pay(new AmmoAmount(amountToPay));
        finalize.run();
    }

    public AmmoAmount newAmmoAvailable(AmmoAmount old){
        return new AmmoAmount(old.subtract(amountToPay));
    }

    String effectString(Actor pov) {
        return String.format("%s paga %s usando cubi", pov.pawn().getUsername(),
                amountToPay.toString());
    }
}
