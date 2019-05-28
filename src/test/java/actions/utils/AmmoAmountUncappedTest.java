package actions.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.*;

class AmmoAmountUncappedTest {
    static AmmoAmountUncapped a,b,c,d;
    static EnumMap<AmmoColor, Integer> a1, b1, c1;
    @BeforeAll
    static void setup(){
        a1 = new EnumMap<>(Map.of(AmmoColor.BLUE, 1,AmmoColor.RED,2, AmmoColor.YELLOW,0));
        b1 = new EnumMap<>(Map.of(AmmoColor.RED, 1,AmmoColor.YELLOW,3));
        c1 = new EnumMap<>(Map.of(AmmoColor.BLUE, 2,AmmoColor.YELLOW,2));
        a = new AmmoAmountUncapped(a1);
        b = new AmmoAmount(b1);
        c = new AmmoAmountUncapped(c1);

    }

    @Test
    void getAmounts() {
        assertEquals(a1, a.getAmounts());
    }

    @Test
    void compareTo() {

    }

    @Test
    void subtract() {
    }

    @Test
    void add() {
        EnumMap<AmmoColor, Integer> sumRes = new EnumMap<>(Map.of(AmmoColor.BLUE, 2,
                AmmoColor.RED, 1, AmmoColor.YELLOW, 5));
        assertEquals(sumRes, b.add(c).getAmounts());
    }

    @Test
    void testAmount(){

    }

    @Test
    void testCapping(){

    }

    @Test
    void testPrint(){

    }


}