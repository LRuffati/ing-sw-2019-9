package network.rmi.server;

import network.ServerInterface;
import network.rmi.client.ClientNetworkRMIInterface;
import network.exception.InvalidLoginException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Methods called by ClientNetworkRMI
 */
public interface ServerRMIInterface extends Remote {
    int mirror(int n) throws RemoteException;
    int close(ClientNetworkRMIInterface client) throws RemoteException;
    String register(ServerInterface user, String username, String color) throws RemoteException, InvalidLoginException;
}
