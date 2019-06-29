package network;

import player.Actor;
import uid.DamageableUID;

import java.io.Serializable;

/**
 * This class contains all the information that link an Actor with the corresponding client
 */
public class Player implements Serializable {
    private transient Actor actor;
    private String username;
    private transient String password;
    private String color;
    private boolean gameMaster;
    private DamageableUID uid;
    private transient String token;

    private transient ServerInterface serverInterface;

    private boolean onLine;

    public Player(String username,
                  String password,
                  String color,
                  boolean gameMaster,
                  String token,
                  ServerInterface serverInterface) {
        this.username = username;
        this.password = password;
        this.color = color;
        this.gameMaster = gameMaster;
        this.token = token;
        this.onLine = true;
        this.serverInterface = serverInterface;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public void setUid(DamageableUID uid) {
        this.uid = uid;
    }

    public DamageableUID getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    public boolean isGameMaster(){
        return gameMaster;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public boolean isOnLine() {
        return onLine;
    }


    @Override
    public String toString() {
        return "Username:\t" + username + "\nColor:\t" + color + "\nPassword:\t" + password + "\nToken:\t" + token+"\n\n";
    }
}