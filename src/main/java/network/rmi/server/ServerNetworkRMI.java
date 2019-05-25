package network.rmi.server;

import controllerresults.ActionResultType;
import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.Database;
import network.ObjectMap;
import network.ServerInterface;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains all the method defined in ServerRMIInterfaces.
 * This class only handle messages received from the Client.
 */
public class ServerNetworkRMI extends UnicastRemoteObject implements ServerRMIInterface{

    public ServerNetworkRMI() throws RemoteException{
        super();
    }

    private boolean checkConnection(String token) throws RemoteException {
        if(!Database.get().getConnectedTokens().contains(token))
            throw new RemoteException("Invalid Token");
        return Database.get().getConnectedTokens().contains(token);
    }

    public void sendPing() throws RemoteException {
        Database d = Database.get();
        for(ServerInterface client :  d.getConnectedTokens().stream().map(d::getNetworkByToken).collect(Collectors.toList())){
            try {
                client.ping();
            }
            catch (ConnectException e) {
                //TODO: notify controller too?
                d.logout(client);
            }
        }
    }

    @Override
    public String register(ServerInterface serverInterface, String username, String color) throws RemoteException, InvalidLoginException {
        //TODO: registration procedure
        return Database.get().login(serverInterface, username, color);
    }

    @Override
    public int mirror(String token, int num) throws RemoteException {
        checkConnection(token);
        System.out.println("Request di mirror\t" + num);
        return num;
    }

    @Override
    public int close(String token) {
        Database.get().logout(token);
        System.out.println("Richiesta di uscita");
        return 0;
    }

    @Override
    public boolean reconnect(ServerInterface client, String token) throws RemoteException {
        if(token == null)
            return false;
        String tokenFromDb = Database.get().login(client, token);
        return tokenFromDb.equals(token);
    }


    @Override
    public ControllerActionResultClient pickTarget(String token, String choiceMakerId, int choice) throws RemoteException{
        checkConnection(token);
        return ObjectMap.get().pickTarg(choiceMakerId, choice);
    }

    @Override
    public ControllerActionResultClient pickWeapon(String token, String weaponChooserId, List<Integer> choice) throws RemoteException{
        checkConnection(token);
        return ObjectMap.get().pickWeapon(weaponChooserId, choice);
    }

    @Override
    public ControllerActionResultClient pickAction(String token, String actionChooserId, int choice) throws RemoteException{
        checkConnection(token);
        return ObjectMap.get().pickAction(actionChooserId, choice);
    }

    @Override
    public Tuple<Boolean, List<TargetView>> showOptionsTarget(String token, String choiceMakerId) throws RemoteException {
        checkConnection(token);
        return ObjectMap.get().showOptionsTarget(choiceMakerId);
    }

    @Override
    public List<WeaponView> showOptionsWeapon(String token, String weaponChooserId) throws RemoteException {
        checkConnection(token);
        return ObjectMap.get().showOptionsWeapon(weaponChooserId);
    }

    @Override
    public Tuple<Boolean, List<ActionView>> showOptionsAction(String token, String actionPickerId) throws RemoteException {
        checkConnection(token);
        return ObjectMap.get().showOptionsAction(actionPickerId);
    }

    @Override
    public GameMapView getMap(String token) throws RemoteException {
        checkConnection(token);
        //TODO: how to generate a SandBoxView?
        return new GameMapView();
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
