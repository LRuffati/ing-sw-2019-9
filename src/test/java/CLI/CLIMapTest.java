package CLI;

import org.junit.jupiter.api.Test;
import viewclasses.GameMapView;

import java.io.FileNotFoundException;

class CLIMapTest {

    GameMapView gmv;

    @Test
    void printTest() throws FileNotFoundException {
        gmv = new GameMapView();
        CLIMap map = new CLIMap(gmv);
        map.printMap();
    }
}
