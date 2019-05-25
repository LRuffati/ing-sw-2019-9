package network;


import controllerresults.ActionResultType;
import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called from outside the package from the client-side.
 */
public interface ClientInterface {
    //List<Events> getEvent();
    int mirror(int num) throws RemoteException;

    int close() throws RemoteException;
    void register() throws RemoteException, InvalidLoginException;
    boolean reconnect(String token) throws RemoteException;

    ControllerActionResultClient pickTarg(String choiceMakerId, int choice) throws RemoteException;
    ControllerActionResultClient pickWeapon(String weaponChooserId, List<Integer> choice) throws RemoteException;
    ControllerActionResultClient pickAction(String actionChooserId, int choice) throws RemoteException;

    Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) throws RemoteException;
    List<WeaponView> showOptionsWeapon(String weaponChooserId) throws RemoteException;
    Tuple<Boolean, List<ActionView>> showOptionsAction(String actionPickerId) throws RemoteException;

    GameMapView getMap() throws RemoteException;
}
