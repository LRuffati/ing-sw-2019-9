package actions.utils;

import org.jetbrains.annotations.NotNull;

public class WildCardAmmo extends AmmoAmountUncapped{
    public WildCardAmmo(){
    }

    @Override
    public boolean canBuy(@NotNull AmmoAmountUncapped cost) {
        return false;
    }

    @Override
    protected boolean canBeBought(@NotNull AmmoAmountUncapped funds) {
        if (zeroAmmo.canBuy(funds))
            return false;
        else
            return true;
    }
}
