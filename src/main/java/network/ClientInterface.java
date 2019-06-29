package network;


import controller.GameMode;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.exception.InvalidLoginException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Methods called by the client to the network layer
 */
public interface ClientInterface {
    /**
     * Method used to test the connection.
     * It sends an integer to the server and wait until the same integer is returned
     */
    int mirror(int num) throws RemoteException;

    /**
     * Method used by the client to notify the server that the connection is going to be closed
     */
    int close() throws RemoteException;

    /**
     * Method used to require a login procedure.
     * @param username the name requested
     * @param password the password used
     * @param color the color requested
     * @return true iif the login operation worked correctly
     * @throws InvalidLoginException this exception contains all the information needed to identify the error
     */
    boolean register(String username, String password, String color) throws RemoteException, InvalidLoginException;

    /**
     * Method used to require a login procedure after a disconnection (the player must have a username and password)
     * @param username the name requested
     * @param password the password used
     * @return A triple containing: a boolean that indicates if the the login operation worked correctly,
     * a boolean that indicates if the game is already started,
     * and a tuple that contains the name and the mode of the game
     * @throws InvalidLoginException this exception contains all the information needed to identify the error
     */
    Tuple3<Boolean, Boolean, Tuple<String, GameMode>> reconnect(String username, String password) throws RemoteException, InvalidLoginException;

    /**
     * Method used to notify the server that a new pick has to be done.
     * @param choiceIf the indentifier of the choice
     * @param choices a list containing all the element picked
     */
    void pick(String choiceIf, List<Integer> choices) throws RemoteException;

    /**
     * Method used to require a new GameMapView
     */
    void getMap() throws RemoteException;

    /**
     * Method used by the client to ask the server for updates
     */
    void poll() throws RemoteException;

    /**
     * Method used during the login phase to vote the mode that is required
     */
    void modeRequest(boolean normalMode) throws RemoteException;
}
