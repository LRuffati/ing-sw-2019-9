package actions.effects;

import actions.targeters.targets.Targetable;
import actions.utils.AmmoAmount;
import board.Sandbox;
import testcontroller.controllermessage.ControllerMessage;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PayTemplate implements EffectTemplate{

    private final AmmoAmount amount;

    public PayTemplate(AmmoAmount amount){

        this.amount = amount;
    }

    @Override
    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer) {
        return consumer.apply(new Sandbox(sandbox, List.of(new PayEffect(amount))));
    }
}
