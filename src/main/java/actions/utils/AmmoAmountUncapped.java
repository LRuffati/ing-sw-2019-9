package actions.utils;

import java.util.EnumMap;
import java.util.Map;

public class AmmoAmountUncapped extends AmmoAmount {
    private final Map<AmmoColor, Integer> amounts;

    AmmoAmountUncapped(Map<AmmoColor, Integer> amountGiven){
        Map<AmmoColor, Integer> temp = new EnumMap<>(amountGiven);
        for (AmmoColor i: AmmoColor.values()){
            Integer amount = temp.putIfAbsent(i,0);
            if (amount!=null && (amount<0)){
                temp.put(i, 0);
            }
        }
        amounts=temp;
    }

    public AmmoAmountUncapped(AmmoAmount capped){
        this.amounts = capped.getAmounts();
    }

    public AmmoAmount cap(){
        return new AmmoAmount(amounts);
    }
    //TODO: aggiungere i metodi
}
