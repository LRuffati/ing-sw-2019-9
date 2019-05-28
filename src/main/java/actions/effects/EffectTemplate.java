package actions.effects;

import controllerresults.ControllerActionResultServer;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.Map;
import java.util.function.Function;

public interface EffectTemplate {
    ControllerActionResultServer spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerActionResultServer> consumer);
}
