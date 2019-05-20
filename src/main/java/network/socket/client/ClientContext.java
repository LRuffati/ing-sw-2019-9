package network.socket.client;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import controllerresults.ActionResultType;
import genericitems.Tuple;
import genericitems.Tuple3;
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


    private boolean reconnected;
    public void setReconnected(boolean reconnected) {
        this.reconnected = reconnected;
    }
    public boolean getReconnected() {
        return reconnected;
    }


    private Tuple<ActionResultType, String> pickElem;
    public void setPickElement(Tuple<ActionResultType, String> result) {
        this.pickElem = result;
    }
    public Tuple<ActionResultType, String> getPickElement() {
        return pickElem;
    }


    private Tuple3<
            Tuple<Boolean, List<Targetable>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionTemplate>>
            > showOptions;
    public void setShowOptions(Tuple3<
            Tuple<Boolean, List<Targetable>>,
            List<WeaponView>,
            Tuple<Boolean, List<ActionTemplate>>
            > showOptions) {
        this.showOptions = showOptions;
    }
    public Tuple<Boolean, List<Targetable>> getShowOptionsTarget(){
        return showOptions.x;
    }
    public List<WeaponView> getShowOptionsWeapon(){
        return showOptions.y;
    }
    public Tuple<Boolean, List<ActionTemplate>> getShowOptionsAction() {
        return showOptions.z;
    }

}
