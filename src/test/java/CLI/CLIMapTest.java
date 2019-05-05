package CLI;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class CLIMapTest {

    @Test
    void printTest() throws FileNotFoundException {
        CLIMap map = new CLIMap();
        map.printMap();
    }
}
