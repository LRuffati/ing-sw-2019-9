package network;


import actions.ActionTemplate;
import controllerresults.ActionResultType;
import genericitems.Tuple;
import network.exception.InvalidLoginException;
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

    Tuple<ActionResultType, String> pickTarg(String choiceMakerId, int choice) throws RemoteException;
    Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, List<Integer> choice) throws RemoteException;
    Tuple<ActionResultType, String> pickAction(String actionChooserId, int choice) throws RemoteException;

    //TODO: TargetbleView?
    Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) throws RemoteException;
    List<WeaponView> showOptionsWeapon(String weaponChooserId) throws RemoteException;
    //TODO: ActionTemplateView?
    Tuple<Boolean, List<ActionTemplate>> showOptionsAction(String actionPickerId) throws RemoteException;
}
