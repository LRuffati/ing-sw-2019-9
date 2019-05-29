package network.socket.client;

import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import genericitems.Tuple3;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

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
    public void setConnectionResult(boolean connectedResult) {
        this.connectedResult = connectedResult;
    }
    public boolean getConnectionResult() {
        return connectedResult;
    }

    private Tuple<Boolean, Boolean> loginException;
    public void setLoginException(boolean wrongUsername, boolean wrongColor) {
        this.loginException = new Tuple<>(wrongUsername, wrongColor);
    }
    public Tuple<Boolean, Boolean> getLoginException() {
        return loginException;
    }

    private ControllerActionResultClient pickElem;
    public void setPickElement(ControllerActionResultClient result) {
        this.pickElem = result;
    }
    public ControllerActionResultClient getPickElement() {
        return pickElem;
    }


    private Tuple3<
            Tuple<Boolean, List<TargetView>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionView>>
            > showOptions;
    public void setShowOptions(Tuple3<
            Tuple<Boolean, List<TargetView>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionView>>
            > showOptions) {
        this.showOptions = showOptions;
    }
    public Tuple<Boolean, List<TargetView>> getShowOptionsTarget(){
        return showOptions.x;
    }
    public List<WeaponView> getShowOptionsWeapon(){
        return showOptions.y;
    }
    public Tuple<Boolean, List<ActionView>> getShowOptionsAction() {
        return showOptions.z;
    }


    private GameMapView gameMapView;
    public void setGameMapView(GameMapView gameMap){
        this.gameMapView = gameMap;
    }

    public GameMapView getGameMapView() {
        return gameMapView;
    }
}
