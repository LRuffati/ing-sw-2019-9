package network.rmi.server;

import controller.GameMode;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.ServerInterface;
import network.exception.InvalidLoginException;
import controller.controllermessage.ControllerMessage;

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
    Tuple3<String, Boolean, Tuple<String, GameMode>> reconnect(ServerInterface user, String username, String password) throws RemoteException, InvalidLoginException;

    ControllerMessage pick(String token, String choiceId, List<Integer> choices) throws RemoteException;

    void getMap(String token) throws RemoteException;

    ControllerMessage poll(String token) throws RemoteException;

    void pingResponse(String token) throws RemoteException;

    void modeRequest(boolean normalMode) throws RemoteException;
}
