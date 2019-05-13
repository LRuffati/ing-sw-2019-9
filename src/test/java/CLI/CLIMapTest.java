package CLI;

import board.GameMap;
import board.Tile;
import gamemanager.GameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Actor;
import uid.TileUID;
import viewclasses.GameMapView;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

class CLIMapTest {

    private GameMap map;
    private GameMapView gmv;
    private List<Actor> actorList;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String tilePath = "src/resources/ammoTile.txt";
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, tilePath, 3);
        }
        catch (FileNotFoundException ignored){
        }
        map = builder.getMap();
        actorList = builder.getActorList();
    }

    @Test
    void printTest() throws FileNotFoundException {
        /*
        Iterator iterator = map.allTiles().iterator();
        TileUID t = (TileUID) iterator.next();
        actorList.get(0).move(t);
        */
        gmv = map.generateView(actorList.get(0).getPawn().getDamageableUID());
        CLIMap map = new CLIMap(gmv);
        map.printMap();
    }
}
