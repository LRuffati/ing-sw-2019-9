package testcontroller.controllerclient;

import controllerresults.ControllerActionResultClient;
import view.View;
import viewclasses.GameMapView;

import java.util.List;

/**
 * Interface implemented by ClientController. It contains all the method that can be used by a View
 */
public interface ClientControllerClientInterface {
    /**
     * This method is called by the View to notify the Server that a new choice has been made.
     * @param elem Contains the type of the action to be performed and an identifier of the action.
     * @param choices A list containing all the index of elements that have been chosen. PickTarget and PickAction only analyze the first element of the List.
     */
    void pick(ControllerActionResultClient elem, List<Integer> choices);
    /**
     * This method is used by the client to restart the action.
     */
    void restartSelection();

    /**
     * This method is used by the client to repeat the last selection
     */
    void rollback();


    /**
     * This method is used to allow a Client to register to the Server for the first time
     * @param username The username chosen by the User. It must be unique
     * @param password The password used to allow reconnection after timeout/disconnection
     * @param color The color chosen by the User
     */
    void login(String username, String password, String color);
    /**
     * This method is used to allow a Client to login after a disconnection
     * @param username The username chosen by the User. It must be unique
     * @param password The password used to allow reconnection after timeout/disconnection
     */
    void login(String username, String password);

    /**
     * This method is used if the Client want to definitively quit the game. No reconnection is allowed after that.
     */
    void quit();

    /**
     * This method returns the last valid GameMap
     */
    GameMapView getMap();

    /**
     * Method used if the View needs to notify the ClientController the new View object
     */
    void attachView(View view);
}
