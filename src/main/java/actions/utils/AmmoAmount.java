package actions.utils;

import gamemanager.ParserConfiguration;
import org.jetbrains.annotations.*;

import java.util.EnumMap;
import java.util.Map;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * This class represents a fixed ammunition amount, it can represent a cost or the amount available to the player
 * <br/>It is important that AmmoAmount be immutable
 */
public class AmmoAmount extends AmmoAmountUncapped implements Comparable<AmmoAmountUncapped> {

    /**
     * The maximum amount available, this is a global variable
     */
    private static Integer maximumAmmo = ParserConfiguration.parseInt("maximumAmmoPerCard");

    /**
     * The amount represented by the class
     */
    private final Map<AmmoColor, Integer> amounts;

    public AmmoAmount(AmmoAmountUncapped a){
        amounts = a.getAmounts();
        for (AmmoColor c: amounts.keySet()){
            if (amounts.get(c) <0){
                amounts.put(c, 0);
            } else if (amounts.get(c)>maximumAmmo){
                amounts.put(c, maximumAmmo);
            }
        }
    }

    /**
     * This method initializes the class by receiving a Map and checking that all the values associated with an element
     * of AmmoColor are between 0 and the max
     * @param amountGiven is an existing map which may or may not have a value for each color
     */
    public AmmoAmount(Map<AmmoColor, Integer> amountGiven){
        Map<AmmoColor, Integer> temp = new EnumMap<>(amountGiven);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0)){
                temp.put(i, 0);
            }
        }
        amounts = temp;
        for (AmmoColor c: amounts.keySet()){
            if (amounts.get(c) <0){
                amounts.put(c, 0);
            } else if (amounts.get(c)>maximumAmmo){
                amounts.put(c, maximumAmmo);
            }
        }
    }

    public AmmoAmount(){
        Map<AmmoColor, Integer> temp = new EnumMap<>(AmmoColor.class);
        for (AmmoColor i: AmmoColor.values()){
            temp.put(i,0);
        }
        amounts=temp;
    }

    /**
     * Returns a Map containing the number of ammo in this AmmoAmount
     * @return a Map containing the number of ammo in this AmmoAmount
     */
    @Override
    public Map<AmmoColor, Integer> getAmounts(){
        return new EnumMap<>(amounts);
    }

    @Override
    public String toString() {
        return "RED:" + getAmounts().get(AmmoColor.RED)
                +" BLUE:" + getAmounts().get(AmmoColor.BLUE)
                +" YELLOW:" + getAmounts().get(AmmoColor.YELLOW);
    }
}
