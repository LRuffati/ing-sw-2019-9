package gamemanager;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ParserWeaponTest {
    private Collection<Weapon> weaponCollection;

    @BeforeEach
    void setup(){
         voidTest();
    }

    @Test
    void voidTest() {
        String path = "weapons.txt";
        weaponCollection = ParserWeapon.parseWeapons(path);
        System.out.println(weaponCollection.size());
        //System.out.println(weaponCollection.size());
    }

    @Test
    void simpleWeaponTest(){
        //System.out.println("DIM\t" + weaponCollection.size());
        assertTrue(weaponCollection.size() > 15);
        Weapon weaponToTest = weaponCollection.iterator().next();
        assertTrue(weaponCollection.size() > 15);

        Map<AmmoColor, Integer> amountGiven = new HashMap<>();
        amountGiven.put(AmmoColor.BLUE,2);
        AmmoAmount amountTest = new AmmoAmount(amountGiven);
        assertEquals("BLU: 1",weaponToTest.getBuyCost().toString());
        assertEquals("BLU: 2",weaponToTest.getReloadCost().toString());
        assertTrue(amountTest.canBuy(weaponToTest.getBuyCost()));

        Map<String, ActionTemplate> actions = weaponToTest.getActions();
        assertTrue(actions.containsKey("main"));


        assertEquals("targ1", weaponToTest.getActions().get("main").getTargeters().iterator().next().x);
        //assertTrue(weaponToTest.getActions().get("main").actionAvailable());
    }

    @Test
    void realFile() {
        String path = "weapons.txt";
       // weaponCollection = ParserWeapon.parseWeapons(path);
        //System.out.println("Dimensione del set di armi:\t" + weaponCollection.size());
    }

    @Test
    void weaponsTest(){


    }

}
