package actions;

import actions.effects.Effect;
import actions.effects.EffectType;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import board.Sandbox;
import genericitems.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ActionTemplate {

    ActionTemplate(ActionInfo info,
                   List<Tuple<String, TargeterTemplate>> targeters, //Order is important
                   Map<EffectType, Collection<Effect>> effects){ //Todo: finish

    }

    Action spawn(Sandbox gameStatus, Map<String, Targetable> existingTargets){
        return null;
    }
}
