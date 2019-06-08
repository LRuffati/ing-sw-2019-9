package network;


import network.exception.InvalidLoginException;
import testcontroller.ControllerMessage;
import viewclasses.GameMapView;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called from outside the package from the client-side.
 */
public interface ClientInterface {
    //List<Events> getEvent();
    int mirror(int num) throws RemoteException;

    int close() throws RemoteException;
    boolean register(String username, String password, String color) throws RemoteException, InvalidLoginException;
    boolean reconnect(String username, String password) throws RemoteException, InvalidLoginException;

    ControllerMessage pick(String choiceIf, List<Integer> choices) throws RemoteException;

    GameMapView getMap(String gameMapId) throws RemoteException;
}
