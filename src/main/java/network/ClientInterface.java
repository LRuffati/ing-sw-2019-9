package network;


import network.exception.InvalidLoginException;

import java.rmi.RemoteException;

/**
 * Methods called from outside the package from the client-side.
 */
public interface ClientInterface {
    //List<Events> getEvent();
    int mirror(int num) throws RemoteException;
    int close() throws RemoteException;
    void register() throws RemoteException, InvalidLoginException;
    boolean reconnect(String token) throws RemoteException;
}
