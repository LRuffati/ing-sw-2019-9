package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import uid.GrabbableUID;

import java.io.Serializable;
import java.util.Map;

/**
 * This class contains all the information of {@link grabbables.Weapon weapons} needed by the client.
 * It only contains getters and setters
 */
public class WeaponView implements Serializable {
    private String name;
    private Map<AmmoColor, Integer> reloadCost;
    private Map<AmmoColor, Integer> buyCost;
    private GrabbableUID uid;
    private Map<String, String> actionDescriptions;

    public WeaponView(){
        name = "";
    }

    public String name() {
        return name;
    }

    public Map<AmmoColor, Integer> buyCost() {
        return buyCost;
    }

    public Map<AmmoColor, Integer> reloadCost() {
        return reloadCost;
    }

    public GrabbableUID uid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuyCost(AmmoAmount buyCost) {
        this.buyCost = buyCost.getAmounts();
    }

    public void setReloadCost(AmmoAmount reloadCost) {
        this.reloadCost = reloadCost.getAmounts();
    }

    public void setUid(GrabbableUID uid) {
        this.uid = uid;
    }

    public void setActionDescriptions(Map<String, String> description) {
        this.actionDescriptions = description;
    }

    public Map<String, String> actionDescription() {
        return actionDescriptions;
    }
}

