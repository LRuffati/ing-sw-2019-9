package viewclasses;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import uid.GrabbableUID;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        try {
            setCard();
        } catch (IOException e) {
            System.out.println("Problema con immagini delle carte.");
        }
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

    private void setCard() throws IOException {
        String path;
        switch (name.toLowerCase()){
            case("martello ionico"):
                path = "gui/Cards/AD_weapons_IT_022.png";
                break;

            case("spada ionica"):
                path = "gui/Cards/AD_weapons_IT_023.png";
                break;

            case("cyberguanto"):
                path = "gui/Cards/AD_weapons_IT_024.png";
                break;

            default:
                path = "gui/Cards/AD_weapons_IT_02.png";
                break;
        }
        card = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
    }
}

