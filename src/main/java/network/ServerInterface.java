package network;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import controllerresults.ControllerActionResult;
import genericitems.Tuple;
import rmi.exceptions.InvalidLoginException;
import rmi.view.RemoteBaseView;
import viewclasses.GameMapView;
import viewclasses.WeaponView;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called from outside the package from the server-side.
 */
public interface ServerInterface {

    void sendUpdate(String str) throws RemoteException;



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


    ControllerActionResult pickAction(String id, int choice);

    ControllerActionResult pickTarget(String id, int choice);

    ControllerActionResult pickWeapon(String id, int[] choice);
    */
}
