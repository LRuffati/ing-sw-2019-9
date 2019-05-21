package gamemanager;

import grabbables.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;

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
        assertTrue(weaponToTest.getActions().containsKey("main"));

    }
}
