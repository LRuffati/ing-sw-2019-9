package controller.controllerclient;

import controller.GameMode;
import network.Player;
import controller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

/**
 * Interface implemented by ClientController. It contains all the method that are called directly by the server.
 * Mostly are notify methods.
 */
public interface ClientControllerNetworkInterface {
    void updateMap(GameMapView gameMapView);

    void onControllerMessage(ControllerMessage controllerMessage);

    void onConnection(Player player, boolean connection, int numOfPlayer);
    void onStarting(String map, GameMode gameMode);
    void onTimer(int ms);
    void onWinner(String winner, int winnerPoints, int yourPoints);


    void reset();
}
