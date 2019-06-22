package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import controller.controllermessage.ControllerMessage;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Gives a certain amound of damage to all BasicTargets targeted by a Target
 */
public class DamageTemplate implements EffectTemplate {

    private final String targetId;
    private final int amount;

    public DamageTemplate(String targetId, int amount){
        this.targetId = targetId;
        this.amount = amount;
    }

    @Override
    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox,
            ControllerMessage> consumer) {
        if (!targets.containsKey(targetId))
            return consumer.apply(sandbox);

        List<Effect> effects = targets.get(targetId).getSelectedPawns(sandbox).stream()
                .filter(i -> !i.equals(targets.get("self").getSelectedPawns(sandbox).iterator().next()))
                .map(i->new DamageEffect(i, amount, false))
                .collect(Collectors.toList());

        return consumer.apply(new Sandbox(sandbox, effects));
    }
}
