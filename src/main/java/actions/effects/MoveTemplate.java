package actions.effects;

import actions.targeters.targets.Targetable;
import board.Sandbox;
import testcontroller.controllermessage.ControllerMessage;
import uid.DamageableUID;
import uid.TileUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class MoveTemplate implements EffectTemplate{

    private final String target;
    private final String destination;

    public MoveTemplate(String target, String destination){

        this.target = target;
        this.destination = destination;
    }

    @Override
    public ControllerMessage spawn(Map<String, Targetable> targets, Sandbox sandbox,
                                              Function<Sandbox, ControllerMessage> consumer) {
        if (!targets.containsKey(target))
            return consumer.apply(sandbox);
        if (!targets.containsKey(destination))
            return  consumer.apply(sandbox);

        Set<DamageableUID> mossi = targets.get(target).getSelectedPawns(sandbox);
        TileUID dest = targets.get(destination).getSelectedTiles(sandbox).iterator().next();
        List<Effect> movesEffs = new ArrayList<>();
        for (DamageableUID t: mossi){
            movesEffs.add(new MoveEffect(t,dest));
        }

        return consumer.apply(new Sandbox(sandbox, movesEffs));
    }
}