package network.rmi.server;

import network.ServerInterface;
import network.exception.InvalidLoginException;
import testcontroller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called by ClientNetworkRMI
 */
public interface ServerRMIInterface extends Remote {
    int mirror(String token, int n) throws RemoteException;
    int close(String token) throws RemoteException;
    String register(ServerInterface user, String username, String password, String color) throws RemoteException, InvalidLoginException;
    String reconnect(ServerInterface user, String username, String password) throws RemoteException, InvalidLoginException;

    ControllerMessage pick(String token, String choiceId, List<Integer> choices) throws RemoteException;

    GameMapView getMap(String token) throws RemoteException;

    ControllerMessage poll(String token) throws RemoteException;

    void pingResponse(String token) throws RemoteException;
}
