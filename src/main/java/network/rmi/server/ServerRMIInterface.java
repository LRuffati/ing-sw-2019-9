package network.rmi.server;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import controllerresults.ActionResultType;
import genericitems.Tuple;
import network.ServerInterface;
import network.exception.InvalidLoginException;
import network.exception.InvalidTokenException;
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
    String register(ServerInterface user, String username, String color) throws RemoteException, InvalidLoginException;
    boolean reconnect(ServerInterface user, String token) throws RemoteException;

    Tuple<ActionResultType, String> pickTarget(String token, String choiceMakerId, int choice) throws RemoteException;
    Tuple<ActionResultType, String> pickWeapon(String token, String weaponChooserId, List<Integer> choice) throws RemoteException;
    Tuple<ActionResultType, String> pickAction(String token, String actionChooserId, int choice) throws RemoteException;

    //TODO: TargetbleView?
    Tuple<Boolean, List<Targetable>> showOptionsTarget(String token, String choiceMakerId) throws RemoteException;
    List<WeaponView> showOptionsWeapon(String token, String weaponChooserId) throws RemoteException;
    //TODO: ActionTemplateView?
    Tuple<Boolean, List<ActionTemplate>> showOptionsAction(String token, String actionPickerId) throws RemoteException;
}
