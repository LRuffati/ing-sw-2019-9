package actions.effects;

import actions.utils.AmmoAmount;

import java.util.Map;

/**
 * This class can be used by the Sandbox to keep track of which effects have been applied at any
 * point in the simulation.
 * By using this class we can enable the controller to easily roll back an action it doesn't wish
 * to pursue anymore
 */
public class EffectTree {


    /**
     * This class provides the information for a single action execution.
     */
    private class EffectNode {
        private final AmmoAmount cost;
        private final Map<EffectType, Effect> effects;

        EffectNode(AmmoAmount cost, Map<EffectType,Effect> effects){
            this.cost = cost;
            this.effects = effects;
        }


        public AmmoAmount getCost() {
            return cost;
        }

        public Map<EffectType, Effect> getEffects() {
            return effects;
        }
    }
}
