package actions.effects;

import player.Actor;
import testcontroller.SlaveController;

public class PayWithAmmoEffect implements Effect {
    @Override
    public EffectType type() {
        return null;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {

    }

    @Override
    public String effectString(Actor pov) {
        return null;
    }
}
