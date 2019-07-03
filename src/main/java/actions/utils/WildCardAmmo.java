package actions.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WildCardAmmo extends AmmoAmountUncapped{


    @Override
    public boolean canBuy(@NotNull AmmoAmountUncapped cost) {
        Map<AmmoColor, Integer> ams = cost.getAmounts();
        Integer amsT = ams.values().stream().reduce(0, Integer::sum);
        if (amsT>1){
            return false;
        }
        return true;
    }

    @Override
    protected boolean canBeBought(@NotNull AmmoAmountUncapped funds) {
        if (zeroAmmo.canBuy(funds))
            return false;
        else
            return true;
    }

    @Override
    public AmmoAmountUncapped subtract(AmmoAmountUncapped c) {
        if (c.canBeBought(AmmoAmountUncapped.zeroAmmo))
            return this;
        else return AmmoAmountUncapped.zeroAmmo;
    }
}
