package network.rmi.client;

import network.rmi.server.ServerRMIInterface;
import network.exception.InvalidLoginException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *  Contains all the method defined in ServerInterface and ClientInterface.
 */
public class ClientNetworkRMI extends UnicastRemoteObject implements ClientNetworkRMIInterface {

    private transient ServerRMIInterface controller;

    public ClientNetworkRMI(ServerRMIInterface controller) throws RemoteException {
        this.controller = controller;
    }

    @Override
    public void sendUpdate(String str) {
        System.out.println("Update:\t" + str);
    }

    @Override
    public int close(int num) throws RemoteException {
        controller.close(this);
        System.out.println("Permesso di uscita");
        return 0;
    }

    @Override
    public int mirror(int num) throws RemoteException{
        int n = controller.mirror(num);
        System.out.println("Mirrored\t" + n);
        return n;
    }

    @Override
    public void register() throws RemoteException, InvalidLoginException {
        String token = controller.register(this, "me", "blue");
    }

    public void run() throws RemoteException, InvalidLoginException {
        register();
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
