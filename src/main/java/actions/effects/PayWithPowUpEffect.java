package actions.effects;

import grabbables.PowerUp;
import player.Actor;
import testcontroller.SlaveController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PayWithPowUpEffect implements Effect{
    private final PowerUp powerup;

    public PayWithPowUpEffect(PowerUp p) {

        powerup = p;
    }

    @Override
    public EffectType type() {
        return EffectType.PAY;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize,
                               Consumer<String> broadcaster) {
        pov.getSelf().pay(powerup);
        broadcaster.accept(effectString(pov.getSelf()));
        finalize.run();
    }

    @Override
    public List<PowerUp> newUsedPowUp(List<PowerUp> old){
        List<PowerUp> newL = new ArrayList<>(old);
        newL.add(powerup);
        return newL;
    }

    String effectString(Actor pov) {
        return String.format("%s paga scartando %s", pov.pawn().getUsername(), powerup.toString());
    }
}
