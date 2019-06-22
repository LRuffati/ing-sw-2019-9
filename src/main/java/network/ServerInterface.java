package network;

import viewclasses.GameMapView;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Methods called from outside the package from the server-side.
 */
public interface ServerInterface extends Remote {

    void setToken(String token) throws RemoteException;



    void sendUpdate(String str) throws RemoteException;

    void sendException(Exception exception) throws RemoteException;

    void ping() throws RemoteException;

    void nofifyMap(GameMapView gameMap) throws RemoteException;



    void onConnection(Player player, int numOfPlayer) throws RemoteException;
    void onDisconnection(Player player, int numOfPlayer) throws RemoteException;

    void onStarting(String map) throws RemoteException;
    void onTimer(int ms) throws RemoteException;
}
