package network;


import rmi.exceptions.InvalidLoginException;

import java.rmi.RemoteException;

/**
 * Methods called from outside the package from the client-side.
 */
public interface ClientInterface {
    //List<Events> getEvent();
    int mirror(int num) throws RemoteException;
    int close(int num) throws RemoteException;
    void register() throws RemoteException, InvalidLoginException;
}
