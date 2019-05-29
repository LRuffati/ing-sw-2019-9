package network;

import viewclasses.GameMapView;

import java.rmi.RemoteException;

/**
 * Methods called from outside the package from the server-side.
 */
public interface ServerInterface {

    void sendUpdate(String str) throws RemoteException;

    void sendException(Exception exception) throws RemoteException;

    void ping() throws RemoteException;

    void nofifyMap(GameMapView gameMap) throws RemoteException;


    /**
     * Used to start a game when minNumber of players are reached
     */
    //boolean startGame();

    /**
     * Returns a GameMapView of the game, based on the token of the caller
     */
    //GameMapView getMap();

    //Status getStatus();

    //TODO: RemoteBaseView or Controller?
    /**
     * Connects the user, given the Username, Color and the RemoteView
     */
    //boolean login(String username, Color color, RemoteBaseView view) throws InvalidLoginException;


    /*
    Tuple<Boolean, List<ActionTemplate>> showAction(String id);

    Tuple<Boolean, List<Targetable>> showOption(String id);

    List<WeaponView> showWeapon(String id);


    ControllerActionResultServer pickAction(String id, int choice);

    ControllerActionResultServer pickTarget(String id, int choice);

    ControllerActionResultServer pickWeapon(String id, int[] choice);
    */
}
