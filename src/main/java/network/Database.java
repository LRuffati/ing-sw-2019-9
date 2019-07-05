package network;

import controller.MainController;
import controller.SlaveController;
import network.exception.InvalidLoginException;
import network.rmi.server.ProxyForRMI;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds all the necessary information to link Players, colors, tokens and Networks.
 */
public class Database {
    private Logger logger = Logger.getLogger(getClass().getSimpleName());

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
    public synchronized String login(ServerInterface network, boolean isrmi, String username, String password, String color) throws InvalidLoginException {
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

        ServerInterface proxy = network;
        try {
            proxy = isrmi ? new ProxyForRMI(network) : network;
        }
        catch (RemoteException e) {
            logger.log(Level.INFO, "login", e);
        }
        Player user = usersByUsername.get(username);
        if(user == null) {
            user = new Player(username, password, color, isFirst, token, proxy);
            usersByUsername.put(token, user);
            networkByToken.put(token, proxy);
            usersByToken.put(token, user);

            try {
                proxy.setToken(token);
            }
            catch (RemoteException e) {
                logger.log(Level.INFO, "login-SetToken", e);
                logout(token);
            }
        }
        colors.remove(color);


        controllerByToken.put(token, mainController.bind(user, proxy));
        mainController.connect(user);

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
    public synchronized String login(ServerInterface network, boolean isrmi, String username, String password) throws InvalidLoginException{
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

        ServerInterface proxy = network;
        try {
            proxy = isrmi ? new ProxyForRMI(network) : network;
        }
        catch (RemoteException e) {
            logger.log(Level.INFO, "login, reconnect", e);
        }
        usersByToken.get(token).setServerInterface(proxy);
        controllerByToken.get(token).setNetwork(proxy);
        try {
            proxy.setToken(token);
        }
        catch (RemoteException e) {
            logger.log(Level.INFO, "login, reconnect-setServerInterface", e);
            logout(token);
        }

        networkByToken.put(token, proxy);
        disconnectedToken.remove(token);

        mainController.reconnect(getUserByToken(token));

        synchronized (wait) {
            connectedToken.add(token);
            TimerForDisconnection.add(token);
        }

        return token;
    }

    /**
     * This method logs the client out.
     * It does not free Username and Color, so the Client can reconnect later using the same token.
     * @param token The token of the caller.
     */
    public synchronized void logout(String token){
        if(token == null || !connectedToken.contains(token))
            return;

        networkByToken.remove(token);
        disconnectedToken.add(token);

        TimerForDisconnection.stop(token);
        connectedToken.remove(token);

        mainController.logout(getUserByToken(token));

        if(connectedToken.isEmpty())
            clearAll();
    }

    /**
     * Same as {@link #logout(String) logout(String)}
     * @param serverInterface The interface of the caller
     */
    public synchronized void logout(ServerInterface serverInterface){
        for(Map.Entry entry : networkByToken.entrySet()){
            if(serverInterface.equals(entry.getValue())) {
                logger.log(Level.INFO, "logout, serverInterface");
                logout((String) entry.getKey());
                break;
            }
        }
    }


    public String gameMaster(){
        return gameMaster;
    }

    /**
     *
     * @return a Set containing all the tokens connected to the game
     */
    public synchronized Set<String> getConnectedTokens() {
        Set<String> ret;
        ret = new HashSet<>(connectedToken);
        return ret;
    }

    /**
     *
     * @return a set containing alle the token currently disconnected
     */
    public synchronized Set<String> getDisconnectedToken() {
        return disconnectedToken;
    }

    /**
     *
     * @return a Collection containing all the Player that logged in
     */
    public Collection<Player> getPlayers() {
        return usersByToken.values();
    }

    /**
     *
     * @return a Collection containing all the SlaveControllers
     */
    public Collection<SlaveController> getSlaveControllers() {
        return controllerByToken.values();
    }


    /**
     * Method used at the end of the game.
     * It clears the memory and prepare the server to start a new Game.
     */
    private void clearAll() {
        Iterator<Player> iterator = usersByToken.values().iterator();
        while(iterator.hasNext()) {
            Player player = iterator.next();
            colors.add(player.getColor());
            ObjectMap.get().clearCache(player.getToken());
            logout(player.getToken());
        }
        usersByToken.clear();
        usersByUsername.clear();
        connectedToken.clear();
        disconnectedToken.clear();
    }
}
