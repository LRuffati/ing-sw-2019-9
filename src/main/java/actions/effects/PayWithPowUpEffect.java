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
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {
        pov.getSelf().pay(powerup);
        finalize.run();
    }

    @Override
    public List<PowerUp> newUsedPowUp(List<PowerUp> old){
        List<PowerUp> newL = new ArrayList<>(old);
        newL.add(powerup);
        return newL;
    }

    @Override
    public String effectString(Actor pov) {
        return String.format("%s paga scartando %s", pov.pawn().getUsername(), powerup.toString());
    }
}
