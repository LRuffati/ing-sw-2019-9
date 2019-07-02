package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;

import java.io.Serializable;
import java.util.Map;

/**
 * This class contains all the information of {@link actions.ActionTemplate actionTemplate} needed by the client.
 * It only contains getters and setters
 */
public class ActionView implements Serializable {
    private String name;
    private String actionId;
    private Map<AmmoColor, Integer> cost;

    public ActionView(String name, String actionId, AmmoAmount cost){
        this.name = name;
        this.actionId = actionId;
        this.cost = cost.getAmounts();
    }

    public String getName() {
        return name;
    }

    public String getActionId() {
        return actionId;
    }

    public Map<AmmoColor, Integer> getCost() {
        return cost;
    }
}
