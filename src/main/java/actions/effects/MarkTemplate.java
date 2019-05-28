package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import controllerresults.ControllerActionResultServer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MarkTemplate implements EffectTemplate{
    private final String targetId;
    private final int amount;

    public MarkTemplate(String targetId, int amount){
        this.targetId = targetId;
        this.amount = amount;
    }

    @Override
    public ControllerActionResultServer spawn(Map<String, Targetable> targets, Sandbox sandbox, Function<Sandbox, ControllerActionResultServer> consumer) {
        if (!targets.containsKey(targetId))
            return consumer.apply(sandbox);

        List<Effect> effects = targets.get(targetId).getSelectedPawns(sandbox).stream()
                .filter(i -> !i.equals(targets.get("self").getSelectedPawns(sandbox).iterator().next()))
                .map(i->new MarkEffect(i, amount))
                .collect(Collectors.toList());

        return consumer.apply(new Sandbox(sandbox, effects));
    }
}
