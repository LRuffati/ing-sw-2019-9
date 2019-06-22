package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import uid.GrabbableUID;


import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Map;

//TODO: the model should not send the Description
/**
 * This class contains the Weapon card that is used by the view and transmitted from the server to the client
 */
public class WeaponView implements Serializable {
    private String name;
    private Map<AmmoColor, Integer> reloadCost;
    private Map<AmmoColor, Integer> buyCost;
    private GrabbableUID uid;
    private BufferedImage card;

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

    public BufferedImage card() {
        return card;
    }

}

