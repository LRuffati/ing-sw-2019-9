package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import testcontroller.controllermessage.ControllerMessage;

import java.util.Map;
import java.util.function.Function;

public interface EffectTemplate {
    ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer);
}
