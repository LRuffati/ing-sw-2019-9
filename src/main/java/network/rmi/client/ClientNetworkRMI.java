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
    private transient String token;

    public ClientNetworkRMI(ServerRMIInterface controller) throws RemoteException {
        this.controller = controller;
    }

    //ServerInterface methods

    @Override
    public void sendUpdate(String str) {
        System.out.println("Update:\t" + str);
    }


    //ClientInterface methods

    @Override
    public void register() throws RemoteException, InvalidLoginException {
        String token = controller.register(this, "me", "blue");
        this.token = token;
        System.out.println("Il mio token\t" + token);
    }

    @Override
    public int close() throws RemoteException {
        controller.close(token);
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
    public boolean reconnect(String token) throws RemoteException {
        return controller.reconnect(this, token);
    }

    public void run() throws RemoteException, InvalidLoginException {
        run(true, null);
    }
    public void run(boolean newConnection, String token) throws RemoteException, InvalidLoginException {
        if(newConnection)
            register();
        else
            System.out.println(reconnect(token));
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
