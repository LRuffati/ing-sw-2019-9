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
        String path = "src/test/java/gamemanager/weaponToTest";
        try{
            weaponCollection = ParserWeapon.parse(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWrongFile(){
        assertThrows(FileNotFoundException.class, () -> ParserWeapon.parse(""));
        assertThrows(FileNotFoundException.class,
                () -> new GameBuilder(null,null,null,"", 1));
    }

    @Test
    void simpleWeaponTest(){
        Weapon weaponToTest = weaponCollection.iterator().next();
        assertEquals(weaponCollection.size(),1);
        assertEquals("precisione asd",weaponToTest.getName());

        Map<AmmoColor, Integer> amountGiven = new HashMap<>();
        amountGiven.put(AmmoColor.BLUE,2);
        AmmoAmount amountTest = new AmmoAmount(amountGiven);
        assertEquals("RED:0 BLUE:2 YELLOW:0",weaponToTest.getBuyCost().toString());
        assertEquals("RED:0 BLUE:1 YELLOW:0",weaponToTest.getReloadCost().toString());
        assertEquals(1, weaponToTest.getBuyCost().compareTo(amountTest));

        Map<String, ActionTemplate> actions = weaponToTest.getActions();
        assertTrue(actions.containsKey("main"));


        //assertEquals("targ1", weaponToTest.getActions().get("main").getTargeters().);
    }
}
