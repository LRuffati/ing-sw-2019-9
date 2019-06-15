package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import testcontroller.controllermessage.ControllerMessage;

import java.util.Map;
import java.util.function.Function;

public interface EffectTemplate {

    /**
     * This method takes the effectTemplate and adds to the record of actions the effects
     * described in the template
     * @param targets all targets previously acquired
     * @param sandbox the latest sandbox
     * @param consumer this action provides the information on what to do with the modified sandbox
     * @return a ControllerMessage
     */
    ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer);
}
