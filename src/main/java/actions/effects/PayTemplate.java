package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import board.Sandbox;
import controllerresults.ControllerActionResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PayTemplate implements EffectTemplate{

    private final AmmoAmount amount;

    PayTemplate(AmmoAmount amount){

        this.amount = amount;
    }

    @Override
    public ControllerActionResult spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox, ControllerActionResult> consumer) {
        return consumer.apply(new Sandbox(sandbox, List.of(new PayEffect(amount))));
    }
}
