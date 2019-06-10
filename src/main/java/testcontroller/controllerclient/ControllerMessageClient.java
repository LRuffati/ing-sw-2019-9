package testcontroller.controllerclient;

import network.Database;
import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import testcontroller.controllerstates.UpdateTypes;
import viewclasses.GameMapView;

import java.util.List;

//todo: implementa ControllerMessage??
public class ControllerMessageClient {

    private SlaveControllerState type;
    private GameMapView sandbox;
    private GameMapView gameMap;

    private ChoiceBoard choiceBoard;

    private boolean sendMap;

    private UpdateTypes updateType;
    private String message;
    List<EffectView> changes;

    public ControllerMessageClient(ControllerMessage controllerMessage, boolean sendMap, String token) {
        this.type = controllerMessage.type();
        this.sandbox = controllerMessage.sandbox().generateView();

        this.choiceBoard = controllerMessage.genView();

        this.sendMap = sendMap;
        this.gameMap = (sendMap)
                ? controllerMessage.gamemap().x.generateView(Database.get().getUserByToken(token).getUid())
                : null;

        updateType = controllerMessage.getMessage().type();
        message = controllerMessage.getMessage().message();
        changes = controllerMessage.getMessage().getChanges();
    }

    public SlaveControllerState type() {
        return type;
    }

    public ChoiceBoard genView() {
        return null;
    }

    public Message getMessage() {
        return null;
    }

    //todo: implementa ControllerMessage, ma mi serve una GMView invece di una Tupla
    public GameMapView gamemap() {
        if(sendMap)
            return gameMap;
        return null;
    }

    //todo: implementa ControllerMessage, ma mi serve una GMView invece di una SandBox
    public GameMapView sandbox() {
        return sandbox;
    }

    //todo: cosa devo farne?
    public ControllerMessage pick(List<Integer> choices) {
        return null;
    }



    public UpdateTypes getUpdateType() {
        return updateType;
    }

    public String message() {
        return message;
    }

    public List<EffectView> changes() {
        return changes;
    }
}
