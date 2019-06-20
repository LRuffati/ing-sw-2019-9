package grabbables;

import actions.effects.DamageEffect;
import actions.effects.Effect;
import actions.effects.MarkEffect;
import actions.utils.AmmoAmount;
import actions.utils.PowerUpType;
import player.Actor;
import testcontroller.SlaveController;
import testcontroller.controllermessage.ControllerMessage;
import uid.DamageableUID;

import java.util.List;
/*
public class TagBack extends PowerUp {
    public TagBack(PowerUpType type, AmmoAmount ammo) {
        super(type, ammo);
    }

    /**
     * Gets the list of effects which were played prior to this call and tells if I can use the
     * powerup
     *
     * @param lastEffects
     * @return true if the effect can be used, false otherwise
     */
/*    @Override
    public boolean canUse(List<Effect> lastEffects) {
        return false;
    }

    /**
     * Uses and discards the powerup
     *
     * @param pov                The player who attacked and is getting the mark
     * @param lastEffects        The damage that pov gave to the target which triggered the grenade
     * @return the controller message to show the use
     */
/*    @Override
    public ControllerMessage usePowup(SlaveController pov, List<Effect> lastEffects, Runnable onPowerupFinalized) {
        DamageEffect attack = (DamageEffect) lastEffects.listIterator().next();
        DamageableUID attacked = attack.uid;
        SlaveController controllerAttacked = pov.main.getSlaveByUID(attacked);

        MarkEffect effect = new MarkEffect(pov.getSelf().pawnID(), 1);

        pov.main.resolveEffect(controllerAttacked, List.of(effect), onPowerupFinalized);

        pov.getSelf().addMark(attacked, 1);


    }
}
*/