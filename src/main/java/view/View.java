package view;

import network.Player;
import testcontroller.Message;
import viewclasses.*;

import java.util.List;

/**
 * This interface contains all the methods used by View classes to communicate with the external world.
 */
public interface View {
    /**
     * This method is used when a target must be chosen by the Client.
     * The response is sent through the pick method
     * @param gameMap The map that refers to the choose
     * @param choiceId The identifier of the selector.
     * @param single True iif only one element can be chosen
     * @param optional True iif an empty list can be chosen
     * @param target A List containing all the element that can be chosen
     */
    void chooseTarget(List<TargetView> target, boolean single, boolean optional, String description, GameMapView gameMap, String choiceId);
    /**
     * This method is used when an action must be chosen by the Client.
     * The response is sent through the pick method
     * @param choiceId The identifier of the selector.
     * @param single True iif only one element can be chosen
     * @param optional True iif an empty list can be chosen
     * @param action A List containing all the element that can be chosen
     */
    void chooseAction(List<ActionView> action, boolean single, boolean optional, String description, String choiceId);
    /**
     * This method is used when an weapon must be chosen by the Client.
     * The response requires a List of object, and is sent through the pick method
     * @param choiceId The identifier of the selector.
     * @param single True iif only one element can be chosen
     * @param optional True iif an empty list can be chosen
     * @param weapon A List containing all the element that can be chosen
     */
    void chooseWeapon(List<WeaponView> weapon, boolean single, boolean optional, String description, String choiceId);
    /**
     * This method is used when a powerUp must be chosen by the Client.
     * The response requires a List of object, and is sent through the pick method
     * @param choiceId The identifier of the selector.
     * @param single True iif only one element can be chosen
     * @param optional True iif an empty list can be chosen
     * @param powerUp A List containing all the element that can be chosen
     */
    void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId);
    /**
     * This method is used when a String must be chosen by the Client.
     * The response requires a List of object, and is sent through the pick method
     * @param choiceId The identifier of the selector.
     * @param single True iif only one element can be chosen
     * @param optional True iif an empty list can be chosen
     * @param string A List containing all the element that can be chosen
     */
    void chooseString(List<String> string, boolean single, boolean optional, String description, String choiceId);


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

    /**
     * Avvisa la view che si può effettuare il login
     */
    void loginNotif();


    /**
     * This notification is used when a player logs in or logs out the game
     * @param player The player that logged-in or out
     * @param connected True if the player logged in, False if the player logged out
     * @param numOfPlayer How many players already logged in
     */
    void onConnection(Player player, boolean connected, int numOfPlayer);

    /**
     * Method used to notify that the game is starting
     * @param map the name of the configuration Map used in the game. Useful for the GUI
     */
    void onStarting(String map);

    /**
     * Method used to notify the user that an action must be performed in a certain amount of time.
     * @param timeToCount Millisecond before timer end.
     */
    void onTimer(int timeToCount);

    /**
     * Method used to notify the user that he has to respawn.
     * The view only need to print a text message representing the event.
     */
    void onRespawn();
    /**
     * Method used to notify the user that he can use a takeback grenade.
     * The view only need to print a text message representing the event.
     */
    void onTakeback();
    /**
     * Method used to notify the user that he can move the Terminator.
     * The view only need to print a text message representing the event.
     * [THIS FUNCTIONALITY IS NOT IMPLEMENTED]
     */
    void onTerminator();
    /**
     * Method used to notify the user that the current flow of actions cannot be continued and a rollback has been made.
     * The view only need to print a text message representing the event.
     */
    void onRollback();


    /**
     * Method used to notify the user of all the effect applied to the map and to the players
     * Message should contain a List of element that only holds a printable message
     */
    void onMessage(Message message);
}
