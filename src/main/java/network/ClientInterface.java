package network;


import controller.GameMode;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.exception.InvalidLoginException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called from outside the package from the client-side.
 */
public interface ClientInterface {
    int mirror(int num) throws RemoteException;

    int close() throws RemoteException;
    boolean register(String username, String password, String color) throws RemoteException, InvalidLoginException;
    Tuple3<Boolean, Boolean, Tuple<String, GameMode>> reconnect(String username, String password) throws RemoteException, InvalidLoginException;

    void pick(String choiceIf, List<Integer> choices) throws RemoteException;

    void getMap() throws RemoteException;

    void poll() throws RemoteException;

    void modeRequest(boolean normalMode) throws RemoteException;
}
