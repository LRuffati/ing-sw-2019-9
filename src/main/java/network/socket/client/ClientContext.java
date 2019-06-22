package network.socket.client;

import genericitems.Tuple;
import controller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;


/**
 * SINGLETON (CLIENT SIDE)
 *
 * Context used by clients to record data
 */
public class ClientContext {
    private static ClientContext instance;

    private ClientContext() {}

    public static synchronized ClientContext get() {
        if (instance == null) {
            instance = new ClientContext();
        }
        return instance;
    }


    private String token;
    public synchronized void setToken(String token) {
        this.token = token;
    }
    public synchronized String getToken() {
        return token;
    }


    private int mirror;
    public synchronized void setMirror(int mirror) {
        this.mirror = mirror;
    }
    public synchronized int getMirror() {
        return mirror;
    }


    private String message;
    public void setTextMessage(String message) {
        this.message = message;
    }
    public String getTextMessage() {
        return message;
    }


    private boolean connectedResult;
    void setConnectionResult(boolean connectedResult) {
        this.connectedResult = connectedResult;
    }
    boolean getConnectionResult() {
        return connectedResult;
    }

    private Tuple<Boolean, Boolean> loginException;
    void setLoginException(boolean wrongUsername, boolean wrongColor) {
        this.loginException = new Tuple<>(wrongUsername, wrongColor);
    }
    Tuple<Boolean, Boolean> getLoginException() {
        return loginException;
    }

    private ControllerMessage pickElem;
    void setPickElement(ControllerMessage result) {
        this.pickElem = result;
    }
    ControllerMessage getPickElement() {
        return pickElem;
    }


    private GameMapView gameMapView;
    public void setGameMapView(GameMapView gameMap){
        this.gameMapView = gameMap;
    }

    public GameMapView getGameMapView() {
        return gameMapView;
    }
}
