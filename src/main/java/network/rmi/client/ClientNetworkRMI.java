package network.rmi.client;

import controllerclient.ClientControllerNetworkInterface;
import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.rmi.server.ServerRMIInterface;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.GameMapView;
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
    private transient ClientControllerNetworkInterface clientController;

    public ClientNetworkRMI(ServerRMIInterface controller) throws RemoteException {
        this.controller = controller;
    }

    //ServerInterface methods

    @Override
    public void sendUpdate(String str) {
        System.out.println("Update:\t" + str);
    }

    @Override
    public void ping() {
        clientController.reset();
        controller.pingResponse(token);
    }

    @Override
    public void sendException(Exception exception) {
        //TODO: restituire il messaggio al controller ??
        //TODO: throw error message
        System.out.println(exception.getMessage());
    }

    @Override
    public void nofifyMap(GameMapView gameMap) {
        clientController.updateMap(gameMap);
    }

    //ClientInterface methods

    @Override
    public boolean register(String username, String password, String color) throws RemoteException, InvalidLoginException {
        String t = controller.register(this, username, password, color);
        this.token = t;
        System.out.println("Il mio token\t" + t);
        return !t.equals("");
    }

    @Override
    public boolean reconnect(String username, String password) throws RemoteException, InvalidLoginException {
        String token = controller.reconnect(this, username, password);
        this.token = token;
        return !token.equals("");
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
    public ControllerActionResultClient pickTarg(String choiceMakerId, int choice) throws RemoteException {
        return controller.pickTarget(token, choiceMakerId, choice);
    }

    @Override
    public ControllerActionResultClient pickWeapon(String weaponChooserId, List<Integer> choice) throws RemoteException {
        return controller.pickWeapon(token, weaponChooserId, choice);
    }

    @Override
    public ControllerActionResultClient pickAction(String actionChooserId, int choice) throws RemoteException {
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

    @Override
    public GameMapView getMap(String gameMapId) throws RemoteException{
        return controller.getMap(token, gameMapId);
    }



    public void run() throws RemoteException, InvalidLoginException {
        run(true, null);
    }
    public void run(boolean newConnection, String token) throws RemoteException, InvalidLoginException {
        if(newConnection)
            register("user", "psw", "blue");
        else
            System.out.println(reconnect("user", "psw"));
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
