package network.rmi.client;

import controllerresults.ActionResultType;
import genericitems.Tuple;
import network.rmi.server.ServerRMIInterface;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *  Contains all the method defined in ServerInterface and ClientInterface.
 */
public class ClientNetworkRMI extends UnicastRemoteObject implements ClientNetworkRMIInterface {

    private transient ServerRMIInterface controller;
    private transient String token;

    public ClientNetworkRMI(ServerRMIInterface controller) throws RemoteException {
        this.controller = controller;
    }


    @Override
    public void sendUpdate(String str) {
        System.out.println("Update:\t" + str);
    }


    //ServerInterface methods
    @Override
    public void ping() {
        return;
    }

    @Override
    public void sendException(Exception exception) {
        //TODO: restituire il messaggio al controller ??
        //TODO: throw error message
        System.out.println(exception.getMessage());
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
        int n = controller.mirror(token, num);
        System.out.println("Mirrored\t" + n);
        return n;
    }

    @Override
    public boolean reconnect(String token) throws RemoteException {
        boolean res = controller.reconnect(this, token);
        this.token = res ? token : null;
        return res;
    }


    @Override
    public Tuple<ActionResultType, String> pickTarg(String choiceMakerId, int choice) throws RemoteException {
        return controller.pickTarget(token, choiceMakerId, choice);
    }

    @Override
    public Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, List<Integer> choice) throws RemoteException {
        return controller.pickWeapon(token, weaponChooserId, choice);
    }

    @Override
    public Tuple<ActionResultType, String> pickAction(String actionChooserId, int choice) throws RemoteException {
        return controller.pickAction(token, actionChooserId, choice);
    }

    @Override
    public Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) throws RemoteException {
        return controller.showOptionsTarget(token, choiceMakerId);
    }

    @Override
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) throws RemoteException {
        return controller.showOptionsWeapon(token, weaponChooserId);
    }

    @Override
    public Tuple<Boolean, List<ActionView>> showOptionsAction(String actionPickerId) throws RemoteException {
        return controller.showOptionsAction(token, actionPickerId);
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
