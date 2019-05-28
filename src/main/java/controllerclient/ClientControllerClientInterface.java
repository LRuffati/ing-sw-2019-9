package controllerclient;

import controllerresults.ControllerActionResultClient;
import network.exception.InvalidLoginException;

import java.rmi.RemoteException;
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
    void pick(ControllerActionResultClient elem, List<Integer> choices) throws RemoteException;
    /**
     * This method is used by the client to restart the action.
     */
    void restartSelection() throws RemoteException;

    /**
     * This method is used by the client to repeat the last selection
     */
    void rollback() throws RemoteException;


    /**
     * This method is used to allow a Client to register to the Server for the first time
     * @param username The username chosen by the User. It must be unique
     * @param password The password used to allow reconnection after timeout/disconnection
     * @param color The color chosen by the User
     * @return true iif the login procedure succeeded
     * @throws InvalidLoginException if the username or the color are already used, this exception is thrown
     */
    boolean login(String username, String password, String color) throws RemoteException, InvalidLoginException;
    /**
     * This method is used to allow a Client to login after a disconnection
     * @param username The username chosen by the User. It must be unique
     * @param password The password used to allow reconnection after timeout/disconnection
     * @return true iif the reconnection procedure succeeded
     * @throws InvalidLoginException if the username or the password are incorrect, this exception is thrown
     */
    boolean login(String username, String password) throws RemoteException, InvalidLoginException;

    /**
     * This method is used if the Client want to definitively quit the game. No reconnection is allowed after that.
     */
    void quit() throws RemoteException;
}
