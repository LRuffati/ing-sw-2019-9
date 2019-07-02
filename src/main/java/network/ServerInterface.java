package network;

import controller.GameMode;
import viewclasses.GameMapView;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Methods called by the server to the network layer
 */
public interface ServerInterface extends Remote {
    /**
     * Used to set the Token of the user. Used only by the network
     */
    void setToken(String token) throws RemoteException;

    /**
     * This method is used to send a string to the client. Used only for testing/debugging
     */
    void sendUpdate(String str) throws RemoteException;

    /**
     * This method is used to send an exception to the client
     */
    void sendException(Exception exception) throws RemoteException;

    /**
     * This method is used to check if the client is still online
     */
    void ping() throws RemoteException;

    /**
     * This method is used to notify the player that a new Map is available
     */
    void nofifyMap(GameMapView gameMap) throws RemoteException;

    /**
     * This method is used to notify the player that someone joined (or rejoined) the game
     */
    void onConnection(Player player, int numOfPlayer) throws RemoteException;

    /**
     * This method is used to notify the player that someone left the game
     */
    void onDisconnection(Player player, int numOfPlayer, boolean lostTurn) throws RemoteException;

    /**
     * This method is used to notify the player that the game is starting
     * @param map an identifier that contains the name of the map used in the game
     * @param gameMode The game moda
     */
    void onStarting(String map, GameMode gameMode) throws RemoteException;

    /**
     * This method is used to notify the player that serverSide a timer has started
     */
    void onTimer(int ms) throws RemoteException;

    /**
     * This method is used at the end of the game.
     * @param winner the name of the winner
     * @param winnerPoints the points of the winner
     * @param yourPoint the points of the player
     */
    void onWinner(String winner, int winnerPoints, int yourPoint) throws RemoteException;
}
