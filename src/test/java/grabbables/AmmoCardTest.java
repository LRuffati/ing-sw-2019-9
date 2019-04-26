package grabbables;

import actions.utils.AmmoAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCardTest {

    @BeforeEach
    void Setup(){

    }

    @Test
    void constructorTest(){
        AmmoAmount am = new AmmoAmount();
        int numOfPu = 1;
        AmmoCard ac = new AmmoCard(am, numOfPu);
        assertEquals(am, ac.getAmmoAmount());
        assertEquals(numOfPu, ac.getNumOfPowerUp());
    }

    @Test
    void toStringTest(){
        AmmoAmount am = new AmmoAmount();
        int numOfPu = 1;
        AmmoCard ac = new AmmoCard(am, numOfPu);
        assertEquals("RED:0 BLUE:0 YELLOW:0 POWERUP:1", ac.toString());
    }

    //TODO Last two tests to be deleted due to useless overrides.
    @Test
    void equalsNullObjectTest(){
        AmmoAmount am = new AmmoAmount();
        int numOfPu = 1;
        AmmoCard ac = new AmmoCard(am, numOfPu);
        AmmoCard second = null;
        assertNotEquals(second, am);
    }

    @Test
    void equalsDifferentClassTest(){
        AmmoAmount am = new AmmoAmount();
        int numOfPu = 1;
        AmmoCard ac = new AmmoCard(am, numOfPu);
        Object second = new ArrayList<>();
        assertNotEquals(second, am);
    }
}
