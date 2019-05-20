package network.rmi.server;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import controllerresults.ActionResultType;
import genericitems.Tuple;
import network.Database;
import network.ObjectMap;
import network.ServerInterface;
import network.exception.InvalidLoginException;
import viewclasses.WeaponView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
    public Tuple<ActionResultType, String> pickTarget(String choiceMakerId, int choice) {
        return ObjectMap.get().pickTarg(choiceMakerId, choice);
    }

    @Override
    public Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, List<Integer> choice) {
        return ObjectMap.get().pickWeapon(weaponChooserId, choice);
    }

    @Override
    public Tuple<ActionResultType, String> pickAction(String actionChooserId, int choice) {
        return ObjectMap.get().pickAction(actionChooserId, choice);
    }

    @Override
    public Tuple<Boolean, List<Targetable>> showOptionsTarget(String choiceMakerId) {
        return ObjectMap.get().showOptionsTarget(choiceMakerId);
    }

    @Override
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) {
        return ObjectMap.get().showOptionsWeapon(weaponChooserId);
    }

    @Override
    public Tuple<Boolean, List<ActionTemplate>> showOptionsAction(String actionPickerId) {
        return ObjectMap.get().showOptionsAction(actionPickerId);
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
