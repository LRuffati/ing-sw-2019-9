package grabbables;

import actions.effects.Effect;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import testcontroller.SlaveController;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllermessage.WaitMessage;

import java.awt.*;
import java.util.List;
public class TagBack extends PowerUp {
    public TagBack(AmmoColor color) {
        super(PowerUpType.TAGBACKGRANADE, color);
    }

    /**
     * Gets the list of effects which were played prior to this call and tells if I can use the
     * powerup
     *
     * @param lastEffects
     * @return true if the effect can be used, false otherwise
     */
    @Override
    public boolean canUse(List<Effect> lastEffects) {
        return false;
    }

    /**
     * Should never be called on a TagBack
     */
    @Override
    public ControllerMessage usePowup(SlaveController pov, List<Effect> lastEffects, Runnable onPowerupFinalized) {
        // This powerup is handled by the main controller. TagBack.usePowup(..) is a mistake
        return new WaitMessage(List.of());
    }
}
