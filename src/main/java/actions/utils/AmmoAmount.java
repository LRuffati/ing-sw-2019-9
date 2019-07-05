package actions.utils;

import exception.AmmoException;
import gamemanager.ParserConfiguration;

import java.awt.*;
import java.util.Map;

/**
 * This class represents a fixed ammunition amount, it can represent a cost or the amount available to the player
 * <br>It is important that AmmoAmount be immutable
 */
public class AmmoAmount extends AmmoAmountUncapped{

    /**
     * The maximum amount available, this is a global variable
     */
    private static Integer maximumAmmo = ParserConfiguration.parseInt("maximumAmmoPerCard");

    public static AmmoAmount redSingleton = new AmmoAmount(Map.of(AmmoColor.RED, 1));
    public static AmmoAmount blueSingleton = new AmmoAmount(Map.of(AmmoColor.BLUE, 1));
    public static AmmoAmount yellowSingleton = new AmmoAmount(Map.of(AmmoColor.YELLOW, 1));

    private static Map<AmmoColor, Integer> limit(AmmoAmountUncapped a){
        Map<AmmoColor, Integer> amounts = a.getAmounts();
        for (AmmoColor c: amounts.keySet()){
            if (amounts.get(c) <0){
                amounts.put(c, 0);
            } else if (amounts.get(c)>maximumAmmo){
                amounts.put(c, maximumAmmo);
            }
        }
        return amounts;
    }

    public AmmoAmount(AmmoAmountUncapped a){
        super(limit(a));
    }

    /**
     * This method initializes the class by receiving a Map and checking that all the values associated with an element
     * of AmmoColor are between 0 and the max
     * @param amountGiven is an existing map which may or may not have a value for each color
     */
    public AmmoAmount(Map<AmmoColor, Integer> amountGiven){
        super(limit(new AmmoAmountUncapped(amountGiven)));
    }

    public AmmoAmount(){
        super();
    }

    /**
     * To be used when only one of the three type of ammo is not zero.
     * @return the color of the type of ammo which amount is more than zero.
     */
    public Color getOnlyColor() throws AmmoException {
        if(getAmounts().get(AmmoColor.YELLOW)+getAmounts().get(AmmoColor.RED)+getAmounts().get(AmmoColor.BLUE)>0) {
            if (getAmounts().get(AmmoColor.YELLOW) == 0 && getAmounts().get(AmmoColor.RED) == 0) {
                return Color.BLUE;
            } else if (getAmounts().get(AmmoColor.YELLOW) == 0 && getAmounts().get(AmmoColor.BLUE) == 0) {
                return Color.RED;
            } else if (getAmounts().get(AmmoColor.RED) == 0 && getAmounts().get(AmmoColor.BLUE) == 0) {
                return Color.YELLOW;
            }
        }
        throw new AmmoException("Non ci sono munizioni");
    }
}
