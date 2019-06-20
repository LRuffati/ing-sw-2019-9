package view.cli;

import board.GameMap;
import testcontroller.ClientController;
import gamemanager.GameBuilder;
import player.Actor;
import viewclasses.GameMapView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;

public class CLIMain {
    public static void main(String[] str){
        GameMap map;
        GameMapView gmv;
        ClientController client = null;
        List<Actor> actorList;
        GameBuilder builder = null;
        try {
            builder = new GameBuilder(
                    null, null, null, null, 5);
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
        }*/
        CLIDemo demo = new CLIDemo(client);
        demo.updateMap(gmv);
        demo.endGame();
    }
}
