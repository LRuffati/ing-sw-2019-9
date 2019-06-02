package controllerclient;

import controllerresults.ControllerActionResultClient;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface contains all the methods used by View classes to communicate with the external world.
 */
public interface View {
    /**
     * This method is used when a target must be chosen by the Client. Only one element can be picked up.
     * The response is sent through the pick method
     * @param gameMap The map that refers to the chose
     * @param elem The identifier of the selector.
     * @param target A List containing all the element that can be chosen
     */
    void chooseTarget(GameMapView gameMap, ControllerActionResultClient elem, List<TargetView> target);
    /**
     * This method is used when an action must be chosen by the Client. Only one element can be picked up.
     * The response is sent through the pick method
     * @param elem The identifier of the selector.
     * @param action A List containing all the element that can be chosen
     */
    void chooseAction(ControllerActionResultClient elem, List<ActionView> action);
    /**
     * This method is used when one or more weapons must be chosen by the Client.
     * The response requires a List of object, and is sent through the pick method
     * @param elem The identifier of the selector.
     * @param weapon A List containing all the element that can be chosen
     */
    void chooseWeapon(ControllerActionResultClient elem, List<WeaponView> weapon) throws RemoteException;

    /**
     * Method that notifies when a rollback is automatically executed
     */
    void rollback();

    /**
     * Method that notifies when the current Action is completed
     */
    void terminated();

    /**
     * This method gives to the View the last valid GameMap
     * @param gameMapView The map
     */
    void updateMap(GameMapView gameMapView);


    /**
     * This method is used as a response to a Login request
     * @param result True iif the login procedure terminated correctly
     * @param invalidUsername True if the username or the password are already used/not the right ones
     * @param invalidColor True if the Color is already used
     */
    void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor);
}
