package network;

import network.exception.InvalidLoginException;

import java.rmi.server.UID;
import java.util.*;
import java.util.stream.StreamSupport;

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

    private final Map<String, Player> usersByToken = new HashMap<>();
    private final Map<String, Player> usersByUsername = new HashMap<>();
    private final Map<String, ServerInterface> networkByToken = new HashMap<>();
    private String gameMaster;
    private List<String> colors;
    private Set<String> disconnectedToken = new HashSet<>();
    private Set<String> connectedToken = new HashSet<>();


    public synchronized Player getUserByToken(String token){
        Player user = usersByToken.get(token);
        if(user == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return user;
    }

    public synchronized ServerInterface getNetworkByToken(String token){
        ServerInterface network = networkByToken.get(token);
        if(network == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return network;
    }

    public synchronized String login(ServerInterface network, String username, String color) throws InvalidLoginException {
        boolean wrongUsername = false;
        boolean wrongColor = false;
        //TODO: add controls
        /*
        if (usersByToken.values().stream().map(Player::getUsername).anyMatch(u -> u.equals(username)))
            wrongUsername = true;
        if (!colors.contains(color))
            wrongColor = true;
        */

        if(wrongColor || wrongUsername)
            throw new InvalidLoginException("Login exception", wrongUsername, wrongColor);

        boolean isFirst = false;
        String token = new UID().toString();
        colors.remove(color);
        if(gameMaster == null) {
            gameMaster = token;
            isFirst = true;
        }

        Player user = usersByUsername.get(username);
        if(user == null) {
            user = new Player(username, color, isFirst, token);
            usersByUsername.put(token, user);
        }
        networkByToken.put(token, network);
        usersByToken.put(token, user);
        connectedToken.add(token);

        return token;
    }

    public synchronized String login(ServerInterface network, String token){
        if(!disconnectedToken.contains(token))
            return "";

        networkByToken.put(token, network);
        disconnectedToken.remove(token);
        connectedToken.add(token);
        return token;
    }


    //TODO: does this allow reconnection ?
    public synchronized void logout(String token){
        //colors.add(getUserByToken(token).getColor());
        //usersByToken.remove(token);
        networkByToken.remove(token);
        connectedToken.remove(token);
        disconnectedToken.add(token);
    }
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
        return connectedToken;
    }

    public Set<String> getDisconnectedToken() {
        return disconnectedToken;
    }
}
