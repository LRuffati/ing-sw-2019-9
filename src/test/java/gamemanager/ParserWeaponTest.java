package gamemanager;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ParserWeaponTest {
    private Collection<Weapon> weaponCollection;

    @BeforeEach
    void setup(){

    }

    @Test
    void voidTest(){
        String path = "test/weaponToTest";
        try{
            weaponCollection = ParserWeapon.parseWeapons(path);
            System.out.println(weaponCollection.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void simpleWeaponTest(){
        assertEquals(1, weaponCollection.size());
        Weapon weaponToTest = weaponCollection.iterator().next();
        assertEquals(weaponCollection.size(),1);
        assertEquals("precisione asd",weaponToTest.getName());

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
    void realFile(){
        String path = "weapons.txt";
        try{
            weaponCollection = ParserWeapon.parseWeapons(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        System.out.println(weaponCollection.size());
    }

}
