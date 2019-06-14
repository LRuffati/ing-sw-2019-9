package network;


import testcontroller.ChoiceBoard;
import testcontroller.controllerclient.ControllerMessageClient;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;

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


    private boolean checkPick(ControllerMessage controllerMessage, List<Integer> choices) {
        ChoiceBoard choiceBoard = controllerMessage.genView();
        if(choices == null) return false;
        if(!choiceBoard.optional && choices.isEmpty()) return false;
        if(choiceBoard.single && choices.size() > 1) return false;
        int max = 0;
        if(choiceBoard.stringViews != null) max = choiceBoard.stringViews.size();
        if(choiceBoard.powerUpViews != null) max = choiceBoard.powerUpViews.size();
        if(choiceBoard.targetViews!= null) max = choiceBoard.targetViews.size();
        if(choiceBoard.weaponViews != null) max = choiceBoard.weaponViews.size();
        if(choiceBoard.actionViews != null) max = choiceBoard.actionViews.size();
        for(int choice : choices)
            if(choice < 0 || choice >= max)
                return false;
        return true;
    }

    private String newID(){
        return new UID().toString();
    }

    private ControllerMessage handlePick(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);

        if(controllerMessage.type().equals(SlaveControllerState.WAIT))
            clearChache();

        return new ControllerMessageClient(controllerMessage, id);
    }

    public ControllerMessage pick(String token, String choiceId, List<Integer> choices) {
        if(!choiceMap.containsKey(choiceId)) {
            return new ControllerMessageClient(Database.get().getControllerByToken(token).getInstruction(), null);
        }
        ControllerMessage message = choiceMap.get(choiceId);
        if(checkPick(message, choices))
            return handlePick(choiceMap.get(choiceId).pick(choices));
        else
            return message;
    }

    public ControllerMessage init(ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);
        return new ControllerMessageClient(controllerMessage, id);
    }
}
