package actions.effects;

import actions.targeters.targets.Targetable;

import java.util.Collection;
import java.util.Map;

public class ActionResult {
    public ActionResult(Map<EffectType, Collection<EffectTemplate>> effects,
                        Map<String, Targetable> previousTargets){
        for (EffectType i: EffectType.values()){
            if (effects.containsKey(i)) break;
        }
    }
}
