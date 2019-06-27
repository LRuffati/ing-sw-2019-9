package actions.utils;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AmmoAmountTest {

    @Test
    void compareTo() {
        //TODO: check for incompatible amounts, eg. 1Y <?> 2B
    }

    @Test
    void testPrint(){

        AmmoAmount testY = new AmmoAmount(Map.of(AmmoColor.YELLOW, 1));
        AmmoAmount testR = new AmmoAmount(Map.of(AmmoColor.RED, 1));
        AmmoAmount testB = new AmmoAmount(Map.of(AmmoColor.BLUE, 1));
        System.out.println(testY.add(testB).canBuy(testY));
    }

}