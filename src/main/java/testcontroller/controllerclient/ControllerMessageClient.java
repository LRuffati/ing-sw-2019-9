package testcontroller.controllerclient;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;

public class ControllerMessageClient implements ControllerMessage{

    public final String id;

    private SlaveControllerState type;
    private ChoiceBoard choiceBoard;
    private Message message;
    private String gameMapHash;
    private GameMapView sandbox;


    public ControllerMessageClient(ControllerMessage controllerMessage, String id) {
        this.id = id;

        this.type = controllerMessage.type();
        this.choiceBoard = controllerMessage.genView();
        this.sandbox = controllerMessage.sandboxView();
        this.gameMapHash = controllerMessage.gamemap();
        this.message = controllerMessage.getMessage();
    }

    @Override
    public SlaveControllerState type() {
        return type;
    }

    @Override
    public ChoiceBoard genView() {
        return choiceBoard;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String gamemap() {
        return gameMapHash;
    }

    @Override
    public GameMapView sandboxView() {
        return sandbox;
    }

    @Override
    public ControllerMessage pick(List<Integer> choices) {
        //FIXME: call the network method, example: return network.pick(id, choices)
        return null;
    }

}
