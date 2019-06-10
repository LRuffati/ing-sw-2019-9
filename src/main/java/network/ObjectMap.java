package network;


import board.GameMap;
import board.Sandbox;
import testcontroller.controllerclient.ControllerMessageClient;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ObjectMap {

    private static ObjectMap ourInstance = new ObjectMap();
    public static ObjectMap get() {
        return ourInstance;
    }

    private String token;

    private Map<String, ControllerMessage> choiceMap = new HashMap<>();

    private Map<String, Sandbox> sandboxMap = new HashMap<>();
    private Map<Integer, GameMap> gameMapMap = new HashMap<>();


    private ObjectMap() {
    }

    public void clearChache(){
        choiceMap.clear();
        sandboxMap.clear();
        gameMapMap.clear();
    }

    private String newID(){
        return new UID().toString();
    }

    //todo: ControllerMessage
    private ControllerMessageClient handlePick(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);
        sandboxMap.put(newID(), controllerMessage.sandbox());

        if(controllerMessage.type().equals(SlaveControllerState.WAIT))
            clearChache();

        boolean sendMap = gameMapMap.containsKey((Integer) controllerMessage.gamemap().y);
        return new ControllerMessageClient(controllerMessage, sendMap, token);
    }

    //todo: implements ControllerMessage
    public ControllerMessageClient pick(String token, String choiceId, List<Integer> choices) {
        this.token = token;

        if(!choiceMap.containsKey(choiceId)) {
            return new ControllerMessageClient(Database.get().getControllerByToken(token).getInstruction(), true, token);
        }
        return handlePick(choiceMap.get(choiceId).pick(choices));
    }

    public GameMapView showGameMap(String gameMapId) {
        return sandboxMap.get(gameMapId).generateView();
    }
}
