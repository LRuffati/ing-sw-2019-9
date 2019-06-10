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

    private Map<String, ControllerMessage> choiceMap = new HashMap<>();

    private ObjectMap() {}

    public void clearChache(){
        choiceMap.clear();
    }

    private String newID(){
        return new UID().toString();
    }


    private ControllerMessage handlePick(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);

        if(controllerMessage.type().equals(SlaveControllerState.WAIT))
            clearChache();

        return new ControllerMessageClient(controllerMessage);
    }

    public ControllerMessage pick(String token, String choiceId, List<Integer> choices) {

        if(!choiceMap.containsKey(choiceId)) {
            return new ControllerMessageClient(Database.get().getControllerByToken(token).getInstruction());
        }
        return handlePick(choiceMap.get(choiceId).pick(choices));
    }
}
