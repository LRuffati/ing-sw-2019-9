package network;

import board.GameMap;
import controller.ClientController;
import controller.MainController;
import gamemanager.GameBuilder;
import network.rmi.RMIServerLauncher;
import network.socket.SocketServerLauncher;
import org.junit.jupiter.api.Test;
import player.Actor;
import view.cli.CLIDemo;
import viewclasses.GameMapView;

import java.io.IOException;
import java.util.List;

public class RMIServerTest {

    @Test
    void voidTest(){
        new RMIServerLauncher(1099);
        try {
            new SocketServerLauncher(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void mainControllerTest(){
        MainController initGame = new MainController();
        //NetworkBuilder.NetworkStarter(initGame);
    }

    @Test
    void cliTest(){
        GameMap map;
        GameMapView gmv;
        ClientController client = null;
        List<Actor> actorList;
        GameBuilder builder = null;
        builder = new GameBuilder(
                null, null, null, null, 5);
        map = builder.getMap();
        actorList = builder.getActorList();
        gmv = map.generateView(actorList.get(0).pawn().getDamageableUID());
        /*try {
            client = new ClientController(true,true,"localhost");
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }*/
        CLIDemo demo = new CLIDemo(client);
        demo.updateMap(gmv, true);
        System.out.println();
        demo.endGame();
    }
}
