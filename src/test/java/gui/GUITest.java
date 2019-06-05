package gui;

import org.junit.jupiter.api.Test;
import view.gui.GUIMap1;

public class GUITest {
    GUIMap1 map;

    @Test
    void emptyMapTest(){
        map = new GUIMap1("src/resources/GUImap1.png");
    }
}
