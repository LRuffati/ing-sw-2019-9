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
import java.rmi.NotBoundException;
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



}
