package controller.controllerclient;

import controller.GameMode;
import network.Player;
import controller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

/**
 * Interface implemented by {@link controller.ClientController ClientController}. It contains all the method that are called directly by the server.
 * Most of them are just notification
 */
public interface ClientControllerNetworkInterface {
    /**
     * This method notifies that a new Map is available
     */
    void updateMap(GameMapView gameMapView);

    /**
     * This method notifies that a new ControllerMessage is available
     */
    void onControllerMessage(ControllerMessage controllerMessage);

    /**
     * This method notifies that a player just logged in or logged out
     */
    void onConnection(Player player, boolean connection, int numOfPlayer, boolean lostTurn);

    /**
     * This method notifies that the game is going to start
     */
    void onStarting(String map, GameMode gameMode);

    /**
     * This method notifies that the game is starting
     */
    void onTimer(int ms);

    /**
     * This method notifies that the game is over. It also contain the winner of te game and his score
     */
    void onWinner(String winner, int winnerPoints, int yourPoints);

    /**
     * This method is used when a ping request is received
     */
    void reset();
}
