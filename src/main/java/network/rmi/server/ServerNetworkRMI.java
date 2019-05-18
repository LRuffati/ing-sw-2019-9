package network.rmi.server;

import network.Database;
import network.ServerInterface;
import network.rmi.client.ClientNetworkRMIInterface;
import rmi.exceptions.InvalidLoginException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains all the method defined in ServerRMIInterfaces.
 * This class only handle messages received from the Client.
 */
public class ServerNetworkRMI extends UnicastRemoteObject implements ServerRMIInterface{

    public ServerNetworkRMI() throws RemoteException{
        super();
    }

    @Override
    public String register(ServerInterface serverInterface, String username, String color) throws RemoteException, InvalidLoginException {
        //TODO: registration procedure
        return Database.get().login(serverInterface, username, color);
    }

    @Override
    public int mirror(int num) {
        System.out.println("Request di mirror\t" + num);
        return num;
    }

    @Override
    public int close(ClientNetworkRMIInterface client) {
        Database.get().logout(client);
        System.out.println("Richiesta di uscita");
        //TODO: how to close a rmi connection?
        //clientHandler.stop();
        return 0;
    }




    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
