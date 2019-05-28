package actions.utils;

import gamemanager.ParserConfiguration;
import org.jetbrains.annotations.*;

import java.util.EnumMap;
import java.util.Map;

import static java.lang.Integer.min;

/**
 * This class represents a fixed ammunition amount, it can represent a cost or the amount available to the player
 * <br/>It is important that AmmoAmount be immutable
 */
public class AmmoAmount implements Comparable<AmmoAmount> {

    /**
     * The maximum amount available, this is a global variable
     */
    private static Integer maximumAmmo = ParserConfiguration.parseInt("maximumAmmoPerCard");

    /**
     * The amount represented by the class
     */
    private final Map<AmmoColor, Integer> amounts;

    /**
     * This method initializes the class by receiving a Map and checking that all the values associated with an element
     * of AmmoColor are between 0 and the max
     * @param amountGiven is an existing map which may or may not have a value for each color
     */
    public AmmoAmount(Map<AmmoColor, Integer> amountGiven){
        Map<AmmoColor, Integer> temp = new EnumMap<>(amountGiven);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0 || amount>maximumAmmo)){
                temp.put(i, Math.min(maximumAmmo, Math.max(amount, 0)));
            }
        }
        amounts=temp;
    }

    public AmmoAmount(){
        Map<AmmoColor, Integer> temp = new EnumMap<>(AmmoColor.class);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0 || amount>maximumAmmo)){
                temp.put(i, Math.min(maximumAmmo, Math.max(amount, 0)));
            }
        }
        amounts=temp;
    }

    /**
     * This method creates a new AmmoAmount to represent a cost having been paid
     * @param c the cost
     * @return the original ammoAmount decreased by c
     */
    public AmmoAmountUncapped subtract(AmmoAmount c){
        if (this.compareTo(c)>0) {
            Map<AmmoColor,Integer> newMap = new EnumMap<>(amounts);
            for (Map.Entry<AmmoColor, Integer> i: c.amounts.entrySet()){
                newMap.put(i.getKey(), amounts.get(i.getKey())-i.getValue());
            }
            return new AmmoAmount(newMap);
        } else throw new IllegalArgumentException("Cost is greater than available amounts");
    }

    /**
     * This method creates a new AmmoAmount to represent a reloaded amount
     * @param r the AmmoAmount to add
     * @return An AmmoAmount which is the pointwise sum of
     */
    public AmmoAmount add(AmmoAmount r){
        Map<AmmoColor,Integer> newMap = new EnumMap<>(amounts);
        for (Map.Entry<AmmoColor, Integer> i: r.amounts.entrySet()){
            newMap.put(i.getKey(), min(3, amounts.get(i.getKey())+i.getValue()));
        }
        return new AmmoAmount(newMap);
    }

    /**
     * Returns a Map containing the number of ammo in this AmmoAmount
     * @return a Map containing the number of ammo in this AmmoAmount
     */
    public Map<AmmoColor, Integer> getAmounts(){
        return new EnumMap<>(amounts);
    }

    /**
     * This functions implements a comparison with the following logic:
     * this &gt; o if the ammo in this can be used to run an action costing o
     * this &lt; o if the ammo in this is not sufficient to cover o
     * @param o The other AmmoAmount
     * @return 1 if I can pay o using this and -1 otherwise
     */
    @Override
    public int compareTo(@NotNull AmmoAmount o) {
        for (AmmoColor i: AmmoColor.values()){
            if (amounts.get(i)<o.amounts.get(i)){
                return -1;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return "RED:" + amounts.get(AmmoColor.RED)
                +" BLUE:" + amounts.get(AmmoColor.BLUE)
                +" YELLOW:" + amounts.get(AmmoColor.YELLOW);
    }
}
