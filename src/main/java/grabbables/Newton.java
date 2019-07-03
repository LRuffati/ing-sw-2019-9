package grabbables;

import actions.effects.Effect;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import controller.SetMessageProxy;
import controller.SlaveController;
import controller.controllermessage.ControllerMessage;

import java.util.List;

//todo: implement this powerUp
/**
 * This class implements the Newton powerUp.
 */
public class Newton extends PowerUp {
    Newton(AmmoColor color){
        super(PowerUpType.TAGBACKGRANADE, color);
    }

    //todo
    @Override
    public boolean canUse(List<Effect> lastEffects) {
        return false;
    }

    //todo
    @Override
    public ControllerMessage usePowup(SetMessageProxy pov, List<Effect> lastEffects,
                                      Runnable onPowerupFinalized) {
        return null;
    }
}
