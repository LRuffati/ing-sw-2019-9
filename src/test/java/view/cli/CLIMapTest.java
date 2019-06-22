package view.cli;

import board.Coord;
import board.GameMap;
import testcontroller.ClientController;
import gamemanager.GameBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Actor;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class CLIMapTest {

    private GameMap map;
    private GameMapView gmv;
    private List<Actor> actorList;
    private ClientController client;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        try {
            builder = new GameBuilder(
                    null, null, null, null, 4);
        }
        catch (FileNotFoundException ignored){
        }
        map = builder.getMap();
        actorList = builder.getActorList();
        gmv = map.generateView(actorList.get(0).pawn().getDamageableUID());
        /*try {
            client = new ClientController(true,true,"localhost");
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
         */
    }


    void printVoidMapTest(){
        CLIDemo demo = new CLIDemo(client);
        demo.getPrintedMap();
    }

    @Test
    void searchTest(){
        gmv = map.generateView(actorList.get(0).pawn().getDamageableUID());
        CLIMap map = new CLIMap(gmv);
        assert(map.searchCharacter('s').getX()==11);
        assert(map.searchCharacter('s').getY()==1);
    }

    void greetingsTest(){
        CLIDemo demo = new CLIDemo(client);
        demo.greetings();
    }

    void printTargetTest(){
        CLIDemo demo = new CLIDemo(client);
        TargetView tw = new TargetView("asd", new ArrayList<>(), new ArrayList<>());
        List<TargetView> targetViewList = new ArrayList<>();
        targetViewList.add(tw);
        demo.printAppliedTarget(targetViewList);
        demo.getPrintedMap();
    }

    @Test
    void movePlayersTest(){
        gmv = map.generateView(actorList.get(0).pawn().getDamageableUID());
        CLIMap map = new CLIMap(gmv);
        Iterator<ActorView> iterator = gmv.players().iterator();
        map.moveActor(iterator.next(), new Coord(0,0));
        map.moveActor(iterator.next(), new Coord(0,0));
        map.moveActor(iterator.next(), new Coord(1,0));
        map.moveActor(iterator.next(), new Coord(0,2));

        map.printMap();
    }
}
