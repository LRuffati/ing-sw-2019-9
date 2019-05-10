package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;

import java.io.Serializable;
import java.util.Map;

//TODO: the model should not send the Description
public class WeaponView implements Serializable {
    private String name;
    private Map<AmmoColor, Integer> reloadCost;
    private Map<AmmoColor, Integer> buyCost;

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



    public void setName(String name) {
        this.name = name;
    }


    public void setBuyCost(AmmoAmount buyCost) {
        this.buyCost = buyCost.getAmounts();
    }

    public void setReloadCost(AmmoAmount reloadCost) {
        this.reloadCost = reloadCost.getAmounts();
    }
}

