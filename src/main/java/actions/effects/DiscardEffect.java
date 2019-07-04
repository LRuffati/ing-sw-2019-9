package actions.effects;

import controller.SlaveController;
import grabbables.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DiscardEffect implements Effect {

    private final PowerUp pw;

    public DiscardEffect(PowerUp pw){

        this.pw = pw;
    }

    /**
     * @return the type of the effect
     */
    @Override
    public EffectType type() {
        return EffectType.DISCARD;
    }

    /**
     * @param pov         the SlaveController linked to the player enacting the effect
     * @param finalize    contains all the instructions to run after the end of the effect. Including
     * @param broadcaster
     */
    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize, Consumer<String> broadcaster) {
        finalize.run(); //I always discard from the powerup manager
    }

    /**
     * Used for paying and just for paying
     *
     * @param old the powerups used prior to this effect
     * @return old with the just used powerup added
     */
    @Override
    public List<PowerUp> newUsedPowUp(List<PowerUp> old) {
        List<PowerUp> newUse = new ArrayList<>(old);
        newUse.add(pw);
        return newUse;
    }
}
