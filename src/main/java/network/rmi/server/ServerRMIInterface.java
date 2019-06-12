package network.rmi.server;

import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.ServerInterface;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called by ClientNetworkRMI
 */
public interface ServerRMIInterface extends Remote {
    int mirror(String token, int n) throws RemoteException;
    int close(String token) throws RemoteException;
    String register(ServerInterface user, String username, String password, String color) throws RemoteException, InvalidLoginException;
    String reconnect(ServerInterface user, String username, String password) throws RemoteException, InvalidLoginException;

    ControllerActionResultClient pickTarget(String token, String choiceMakerId, int choice) throws RemoteException;
    ControllerActionResultClient pickWeapon(String token, String weaponChooserId, List<Integer> choice) throws RemoteException;
    ControllerActionResultClient pickAction(String token, String actionChooserId, int choice) throws RemoteException;

    Tuple<Boolean, List<TargetView>> showOptionsTarget(String token, String choiceMakerId) throws RemoteException;
    List<WeaponView> showOptionsWeapon(String token, String weaponChooserId) throws RemoteException;
    Tuple<Boolean, List<ActionView>> showOptionsAction(String token, String actionPickerId) throws RemoteException;

    GameMapView getMap(String token, String gameMapId) throws RemoteException;

    void pingResponse(String token);
}
