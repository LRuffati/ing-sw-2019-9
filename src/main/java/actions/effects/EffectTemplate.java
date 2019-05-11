package actions.effects;

import controllerresults.ControllerActionResult;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.Map;
import java.util.function.Function;

public interface EffectTemplate {
    ControllerActionResult spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerActionResult> consumer);
}
