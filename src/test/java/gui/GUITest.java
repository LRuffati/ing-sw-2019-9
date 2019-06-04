package gui;

import org.junit.jupiter.api.Test;
import view.gui.GUIMap;

public class GUITest {
    GUIMap map;

    @Test
    void emptyMapTest(){
        map = new GUIMap("src/resources/GUImap1.png");
    }
}
