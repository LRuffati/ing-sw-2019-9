package actions.utils;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represents an ammunition amount, differently from {@link AmmoAmount AmmoAmount} this is not capped to 3 ammo per color
 * All methods in this class are pure and return value should always replace the variable if an
 * update is intended
 */
public class AmmoAmountUncapped{

    /**
     * An ammoamount which can pay nothing except itself but can be paid by everything
     */
    public static AmmoAmountUncapped zeroAmmo = new AmmoAmountUncapped();

    private final Map<AmmoColor, Integer> amounts;

    public AmmoAmountUncapped(Map<AmmoColor, Integer> amountGiven){
        Map<AmmoColor, Integer> temp = new EnumMap<>(amountGiven);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0)){
                temp.put(i, 0);
            }
        }
        amounts=temp;
    }

    public AmmoAmountUncapped() {
        Map<AmmoColor, Integer> temp = new EnumMap<>(AmmoColor.class);
        for (AmmoColor c: AmmoColor.values()){
            temp.put(c, 0);
        }
        amounts = temp;
    }

    public Map<AmmoColor, Integer> getAmounts(){
        return new EnumMap<>(amounts);
    }

    /**
     * Checks if this ammoAmount is greater than cost
     * @param cost the cost of the object
     * @return true iif this &gt;= cost
     */
    public boolean canBuy(@NotNull AmmoAmountUncapped cost){
        return cost.canBeBought(this);
    }

    /**
     * Checks if this ammoAmount is lower than funds
     * @param funds the ammo available
     * @return true iif this &lt; funds
     */
    protected boolean canBeBought(@NotNull AmmoAmountUncapped funds){
        for (AmmoColor i: AmmoColor.values()){
            if (getAmounts().get(i)>funds.getAmounts().get(i)){
                return false;
            }
        }
        return true;
    }


    /**
     * This method creates a new AmmoAmount to represent a cost having been paid
     * @param c the cost
     * @return the original ammoAmount decreased by c
     */
    public AmmoAmountUncapped subtract(AmmoAmountUncapped c){
        if (!canBuy(c)){ // c is
            throw new IllegalArgumentException("Cost is greater than available amounts");
        } else {
            Map<AmmoColor,Integer> newMap = new EnumMap<>(getAmounts());
            for (Map.Entry<AmmoColor, Integer> i: c.getAmounts().entrySet()){
                newMap.put(i.getKey(), getAmounts().get(i.getKey())-i.getValue());
            }
            return new AmmoAmountUncapped(newMap);
        }
    }

    /**
     * This method creates a new AmmoAmount to represent a reloaded amount
     * @param r the AmmoAmount to add
     * @return An AmmoAmount which is the pointwise sum of
     */
    public AmmoAmountUncapped add(AmmoAmountUncapped r){
        Map<AmmoColor,Integer> newMap = new EnumMap<>(getAmounts());
        for (Map.Entry<AmmoColor, Integer> i: r.getAmounts().entrySet()){
            newMap.put(i.getKey(), getAmounts().get(i.getKey())+i.getValue());
        }
        return new AmmoAmountUncapped(newMap);
    }

    @Override
    public String toString() {

        Map<AmmoColor, Integer> ams = getAmounts();
        int blue = ams.getOrDefault(AmmoColor.BLUE,0);
        int yellow = ams.getOrDefault(AmmoColor.YELLOW,0);
        int red = ams.getOrDefault(AmmoColor.RED,0);

        if ((blue+yellow+red)==0)
            return "0";

        String returnS = "";

        if (blue>0)
            returnS = returnS.concat(String.format("BLU: %d, ", blue));

        if (yellow>0)
            returnS = returnS.concat(String.format("GIALLO: %d, ", yellow));

        if (red>0)
            returnS = returnS.concat(String.format("ROSSO: %d, ", red));

        returnS=returnS.substring(0, returnS.length()-2);
	return returnS;
    }
}
