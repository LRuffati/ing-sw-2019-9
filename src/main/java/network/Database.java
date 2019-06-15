package network;

import controller.MainController;
import controller.SlaveController;
import network.exception.InvalidLoginException;
import uid.DamageableUID;

import java.rmi.server.UID;
import java.util.*;

/**
 * This class holds all the necessary information to link Players, colors, tokens and Networks.
 */
public class Database {
    private static Database ourInstance = new Database();
    public static Database get() {
        return ourInstance;
    }

    private Database() {
        colors = new ArrayList<>(List.of("green", "blue", "gray", "yellow", "pink"));
        gameMaster = null;
    }

    private MainController mainController;
    private final Map<String, SlaveController> controllerByToken = new HashMap<>();

    private final Map<String, Player> usersByToken = new HashMap<>();
    private final Map<String, Player> usersByUsername = new HashMap<>();
    private final Map<String, ServerInterface> networkByToken = new HashMap<>();

    private String gameMaster;
    private List<String> colors;
    private Set<String> disconnectedToken = new HashSet<>();
    private Set<String> connectedToken = new HashSet<>();
    private final Object wait = new Object();


    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    /**
     * @return Returns the Player bound to the player
     * @throws  IllegalArgumentException If the token does not exists, an Exception is thrown
     */
    public synchronized Player getUserByToken(String token){
        Player user = usersByToken.get(token);
        if(user == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return user;
    }

    /**
     * @return Returns the NetworkInterface bound to the player
     * @throws  IllegalArgumentException If the token does not exists, an Exception is thrown
     */
    public synchronized ServerInterface getNetworkByToken(String token){
        ServerInterface network = networkByToken.get(token);
        if(network == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return network;
    }

    public ServerInterface getNetwordByDamageableUid(DamageableUID uid){
        for(String token : connectedToken) {
            if(getUserByToken(token).getUid().equals(uid))
                return getNetworkByToken(token);
        }
        throw new IllegalArgumentException("Invalid uid");
    }

    public synchronized SlaveController getControllerByToken(String token){
        SlaveController controller = controllerByToken.get(token);
        if(controller == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return controller;
    }

    /**
     * This method associates the connection with the player.
     * If the username and the color are not used by other players it generates an unique token and a new user.
     * @param network The Interface that must be used to send messages to the Client
     * @param username The name whom he wants to be called
     * @param password A user-set password used for reconnection
     * @param color The color whom he wants to be associated with
     * @return The token associated with the user
     * @throws InvalidLoginException If username and/or color are already used, this Exception is thrown
     */
    public synchronized String login(ServerInterface network, String username, String password, String color) throws InvalidLoginException {
        boolean wrongUsername = false;
        boolean wrongColor = false;

        if (usersByToken.values().stream().map(Player::getUsername).anyMatch(u -> u.equals(username)))
            wrongUsername = true;
        if (!colors.contains(color))
            wrongColor = true;
        if(wrongColor || wrongUsername)
            throw new InvalidLoginException("Connection exception", wrongUsername, wrongColor);

        if(!mainController.canConnect())
            throw new InvalidLoginException("Game already started", false, false);


        String token = new UID().toString();


        boolean isFirst = false;
        if(gameMaster == null) {
            gameMaster = token;
            isFirst = true;
        }

        Player user = usersByUsername.get(username);
        if(user == null) {
            user = new Player(username, password, color, isFirst, token, network);
            usersByUsername.put(token, user);
            networkByToken.put(token, network);
            usersByToken.put(token, user);

            network.setToken(token);
        }
        colors.remove(color);

        controllerByToken.put(token, mainController.connect(user));

        synchronized (wait) {
            connectedToken.add(token);

            TimerForDisconnection.add(token);
        }

        return token;
    }

    /**
     * This method is used to reconnect a player that left before
     * @param network The Interface that must be used to send messages to the Client
     * @param username The name whom he wants to be called
     * @param password A user-set password used to check the identity of the Client
     * @return The token used by the Client before the disconnection. An empty string is returned in case of errors.
     */
    public synchronized String login(ServerInterface network, String username, String password) throws InvalidLoginException{
        boolean present = false;
        String token = "-1";
        for(String t : disconnectedToken) {
            if (getUserByToken(t).getUsername().equals(username)
                    && getUserByToken(t).getPassword().equals(password)) {
                token = t;
                present = true;
            }
        }
        if(!present)
            throw new InvalidLoginException("Reconnection exception", true, false);

        networkByToken.put(token, network);
        disconnectedToken.remove(token);

        mainController.reconnect(getUserByToken(token));

        synchronized (wait) {
            connectedToken.add(token);
            TimerForDisconnection.add(token);
        }


        return token;
    }

    /**
     * This method is used to allow a Client to permanently quit the game.
     * It frees the Username and Color, so the Client can not reconnect later
     * @param token The token of the caller.
     */
    public synchronized void quit(String token) {
        System.out.println("quit request");
        logout(token);

        /*colors.add(getUserByToken(token).getColor());
        usersByToken.remove(token);
        networkByToken.remove(token);

        connectedToken.remove(token);
        disconnectedToken.remove(token);

        TimerForDisconnection.stop(token);*/
    }

    /**
     * This method logs the client out.
     * It does not free Username and Color, so the Client can reconnect later using the same token.
     * @param token The token of the caller.
     */
    public synchronized void logout(String token){
        if(token == null || !connectedToken.contains(token))
            return;


        synchronized (wait) {
            connectedToken.remove(token);
            TimerForDisconnection.stop(token);
        }

        mainController.logout(getUserByToken(token));

        networkByToken.remove(token);
        disconnectedToken.add(token);

    }

    /**
     * Same as {@link #logout(String) logout(String)}
     * @param serverInterface The interface of the caller
     */
    public synchronized void logout(ServerInterface serverInterface){
        for(Map.Entry entry : networkByToken.entrySet()){
            if(serverInterface.equals(entry.getValue())) {
                logout((String) entry.getKey());
                break;
            }
        }
    }


    public String gameMaster(){
        return gameMaster;
    }

    public Set<String> getConnectedTokens(){
        Set<String> ret;
        synchronized (wait) {
            ret = new HashSet<>(connectedToken);
        }
        return ret;
    }

    public Set<String> getDisconnectedToken() {
        return disconnectedToken;
    }

    public Collection<Player> getPlayers() {
        return usersByToken.values();
    }

    public Collection<SlaveController> getSlaveControllers() {
        return controllerByToken.values();
    }
}
