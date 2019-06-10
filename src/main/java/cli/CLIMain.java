package cli;

import board.GameMap;
import controllerclient.ClientController;
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
        String tilePath = "src/resources/ammoTile.txt";
        String mapPath = "src/resources/map1.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, null, tilePath, 5);
        }
        catch (FileNotFoundException ignored){
        }
        map = builder.getMap();
        actorList = builder.getActorList();
        gmv = map.generateView(actorList.get(0).getPawn().getDamageableUID());
        try {
            client = new ClientController(true,true,"localhost");
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
        CLIDemo demo = new CLIDemo(client);
        demo.endGame();
    }
}
