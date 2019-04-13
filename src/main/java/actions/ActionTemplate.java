package actions;

import actions.effects.Effect;
import actions.effects.EffectType;
import actions.targeters.TargeterTemplate;
import actions.targeters.targets.Targetable;
import board.Sandbox;

import java.util.Collection;
import java.util.Map;

public class ActionTemplate {

    ActionTemplate(ActionInfo info,
                   Map<String, TargeterTemplate> targeters,
                   Map<EffectType, Collection<Effect>> effects){ //Todo: finish

    }

    Action spawn(Sandbox gameStatus, Map<String, Targetable> existingTargets){
        return null;
    }
}
