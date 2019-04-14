package actions.utils;

import org.jetbrains.annotations.*;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents a fixed ammunition amount, it can represent a cost or the amount available to the player
 */
public class AmmoAmount implements Comparable<AmmoAmount> {

    /**
     * The maximum amount available, this is a global variable
     * TODO: load from configuration file
     */
    private static Integer maximumAmmo = 3;


    /**
     * The amount represented by the class
     */
    private final Map<AmmoColor, Integer> amounts;

    /**
     * This method initializes the class by receiving a Map and checking that all the values associated with an element
     * of AmmoColor are between 0 and the max
     * @param amountGiven is an existing map which may or may not have a value for each color
     */
    AmmoAmount(Map<AmmoColor, Integer> amountGiven){
        Map<AmmoColor, Integer> temp = new EnumMap<>(amountGiven);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0 || amount>maximumAmmo)){
                temp.put(i, Math.min(maximumAmmo, Math.max(amount, 0)));
            }
        }
        amounts=temp;
    }

    /**
     * This functions implements a comparison with the following logic:
     * a &gt; o if the ammo in a can be used to run an action costing o
     * a &lt; o if the ammo in a is not sufficient to cover o
     * @param o The other AmmoAmount
     * @return 1 if I can pay o using a and -1 otherwise
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
}
