package rmi.model;

import rmi.controller.Token;
import network.exception.InvalidLoginException;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class Database {

    private Logger logger = Logger.getLogger(Database.class.getName());

    private static Database ourInstance = new Database();
    public static Database getInstance() {
        return ourInstance;
    }


    private Database() {
        colors = new ArrayList<>(List.of("green", "blue", "gray", "yellow", "pink"));
        gameMaster = null;
    }

    private final Map<Token, User> usersByToken = new HashMap<>();
    private final Map<Token, User> usersByName = new HashMap<>();
    private Token gameMaster;
    private List<String> colors;

    public synchronized User getUserbyToken(Token token){
        User user = usersByToken.get(token);
        if(user == null)
            throw new IllegalArgumentException("Invalid token " + token);
        return user;
    }

    public synchronized Token login(String username, String color) throws RemoteException, InvalidLoginException{
        boolean wrongUsername = false;
        boolean wrongColor = false;
        if (usersByToken.values().stream().map(User::getUsername).anyMatch(u -> u.equals(username)))
            wrongUsername = true;
        if (!colors.contains(color))
            wrongColor = true;

        if(wrongColor || wrongUsername)
            throw new InvalidLoginException("Login exception", wrongUsername, wrongColor);

        boolean isFirst = false;
        Token token = new Token();
        colors.remove(color);
        if(gameMaster == null) {
            gameMaster = token;
            isFirst = true;
        }

        //TODO: why is this needed?
        User user = usersByName.get(username);
        if(user == null) {
            user = new User(username, color, isFirst);
            usersByName.put(token, user);
        }
        usersByToken.put(token, user);

        return token;
    }

    //TODO: does this allow reconnection ?
    public synchronized void logout(Token token){
        colors.add(getUserbyToken(token).getColor());
        usersByToken.remove(token);
    }


    public Token gameMaster(){
        return gameMaster;
    }

    public Set<Token> getTokens(){
        return usersByToken.keySet();
    }
}
