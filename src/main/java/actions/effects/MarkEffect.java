package actions.effects;

import player.Actor;
import testcontroller.SlaveController;
import uid.DamageableUID;

public class MarkEffect implements Effect{
    private final int amount;
    private final DamageableUID uid;

    public MarkEffect(DamageableUID i, int amount) {
        this.amount = amount;
        this.uid = i;
    }

    @Override
    public EffectType type() {
        return EffectType.MARK;
    }

    @Override
    public void mergeInGameMap(SlaveController pov, Runnable finalize) {

    }

    @Override
    public String effectString(Actor pov) {
        return null;
    }
}

//TODO @LORENZO

/*

pov.getSelf().getGm().getPawn(uid).getActor().addMark(pov.getSelf().pawnID(), amount);

public void mergeInGameMap(SlaveController pov, Runnable finalize) {
@Override
     * @param finalize contains all the instructions to run after the end of the effect. Contains
        * @param pov
/**


@Override
public String effectString(Actor pov) {
        return String.format("%s ha dato %d marchi a %s", pov.name(), amount,
        pov.getGm().getPawn(uid).getActor().name());
        }
        }

*/