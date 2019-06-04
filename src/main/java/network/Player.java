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
    private String password;
    private String color;
    private boolean gameMaster;
    private DamageableUID uid;
    private String token;

    private boolean onLine;

    public Player(String username, String password, String color, boolean gameMaster, String token) {
        this.username = username;
        this.password = password;
        this.color = color;
        this.gameMaster = gameMaster;
        this.token = token;
        this.onLine = true;
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


    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public boolean isOnLine() {
        return onLine;
    }
}