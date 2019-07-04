package network;


import controller.ChoiceBoard;
import controller.controllerclient.ControllerMessageClient;
import controller.controllermessage.ControllerMessage;
import controller.controllerstates.SlaveControllerState;

import java.rmi.server.UID;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class is used to hold all the {@link controller.controllermessage.ControllerMessage messages} that have been sent to every client and that can be reused.
 * Every player have a dedicated list that holds all the messages that can be chosen.
 * The list is cleared when needed, so only available actions can be made
 */
public class ObjectMap {
    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    private static ObjectMap ourInstance = new ObjectMap();
    public static ObjectMap get() {
        return ourInstance;
    }

    /**
     * This Map contains the ControllerMessage associated with each String identifier
     */
    private Map<String, ControllerMessage> choiceMap = new HashMap<>();
    /**
     * This Map contains all the ControllerMessages identifier associated with each Token
     */
    private Map<String, List<String>> choicesForPlayer = new HashMap<>();

    private Set<String> canNotSend = new HashSet<>();

    private ObjectMap() {}


    private void clearChachePr(String token){
        if(choicesForPlayer.get(token) == null)
            return;
        for(String controllerMessageID : choicesForPlayer.get(token))
            choiceMap.remove(controllerMessageID);
        choicesForPlayer.remove(token);
    }

    private String put(String token, ControllerMessage controllerMessage) {
        String id = newID();
        choiceMap.put(id, controllerMessage);
        if(choicesForPlayer.containsKey(token)) {
            ArrayList<String> list = new ArrayList<>(choicesForPlayer.get(token));
            list.add(id);
            choicesForPlayer.replace(token, list);
        }
        else
            choicesForPlayer.put(token, List.of(id));

        return id;
    }


    private boolean checkPick(ControllerMessage controllerMessage, List<Integer> choices) {
        ChoiceBoard choiceBoard = controllerMessage.genView();
        if(choices == null) return false;
        if(!choiceBoard.optional && choices.isEmpty()) return false;
        if(choiceBoard.single && choices.size() > 1) return false;

        int max = choiceBoard.getNumOfElems();
        for(int choice : choices)
            if(choice < 0 || choice >= max)
                return false;
        return true;
    }

    private String newID(){
        return new UID().toString();
    }



    private ControllerMessage handlePick(ControllerMessage controllerMessage, String token) {

        String id = put(token, controllerMessage);
        if(controllerMessage.type().equals(SlaveControllerState.WAIT))
            clearChachePr(token);

        return new ControllerMessageClient(controllerMessage, id);
    }


    /**
     * This method handles the pick requests of the clients.
     * First of all it checks if the Client can make the pick, and then if che choice is valid.
     * If the client is not blocked the next ControllerMessage is sent, otherwise the client will receive a WAIT message.
     * @param token token of the client
     * @param choiceId id of the ControllerMessage
     * @param choices A List containing all the element chosen
     * @return A rollback if the choice is not valid, a WAIT if the client can't do the action or if the client is blocked, the next ControllerMessage otherwise
     */
    public ControllerMessage pick(String token, String choiceId, List<Integer> choices) {
        if(!choiceMap.containsKey(choiceId)
                || !choicesForPlayer.containsKey(token)
                || !choicesForPlayer.get(token).contains(choiceId) ) {
            return new ControllerMessageClient();
        }
        ControllerMessage message = choiceMap.get(choiceId);
        if(checkPick(message, choices))
            message = handlePick(message.pick(choices), token);
        else
            message = new ControllerMessageClient(message, choiceId, SlaveControllerState.ROLLBACK);

        if(canNotSend.contains(token))
            return new ControllerMessageClient();
        else
            return message;
    }


    /**
     * This method handles the poll request.
     * First of all it removes the client from the set of blocked.
     * If the message is a WAIT one it sends a new wait message
     * Otherwise it adds the message to the list of available messages
     * @param token tje token of the caller
     * @param controllerMessage a message given by SlaveController
     * @return a controllerMessage that contains the next action
     */
    public ControllerMessage poll(String token, ControllerMessage controllerMessage) {
        canNotSend.remove(token);
        if(! controllerMessage.type().equals(SlaveControllerState.WAIT)) {
            String id = put(token, controllerMessage);
            return new ControllerMessageClient(controllerMessage, id);
        }
        else
            return new ControllerMessageClient(controllerMessage, null);
    }


    /**
     * This methods add the token to the blackList and removes all the ControllerMessages already associated
     * @param token the token of the player
     */
    public void clearCache(String token) {
        clearChachePr(token);
    }
}
