package network;


import board.Sandbox;
import testcontroller.ChoiceBoard;
import testcontroller.ControllerMessage;
import testcontroller.controllerclient.ControllerMessageClient;
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

    private Map<String, ControllerMessage> choiceMap = new HashMap<>();



    private Map<String, Sandbox> sandboxMap = new HashMap<>();

    private ObjectMap() {
    }

    public void clearChache(){
        choiceMap.clear();
        sandboxMap.clear();
    }

    private String newID(){
        return new UID().toString();
    }

    private ControllerMessage handlePick(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);
        sandboxMap.put(newID(), controllerMessage.sandbox());

        if(controllerMessage.type().equals(SlaveControllerState.WAIT))
            clearChache();

        return new ControllerMessageClient(controllerMessage);
    }

    public ControllerMessage pick(String choiceId, List<Integer> choices) {
        if(!choiceMap.containsKey(choiceId)) {
            //todo: return error
        }
        return handlePick(choiceMap.get(choiceId).pick(choices));
    }

    public GameMapView showGameMap(String gameMapId) {
        return sandboxMap.get(gameMapId).generateView();
    }

}
