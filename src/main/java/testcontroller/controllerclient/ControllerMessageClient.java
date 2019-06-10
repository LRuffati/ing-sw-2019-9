package testcontroller.controllerclient;

import testcontroller.ChoiceBoard;
import testcontroller.Message;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.util.List;

public class ControllerMessageClient implements ControllerMessage{

    private SlaveControllerState type;
    private ChoiceBoard choiceBoard;
    private Message message;
    private String gameMapHash;
    private GameMapView sandbox;


    public ControllerMessageClient(ControllerMessage controllerMessage) {
        this.type = controllerMessage.type();
        this.choiceBoard = controllerMessage.genView();
        this.sandbox = controllerMessage.sandbox();
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
    public GameMapView sandbox() {
        return sandbox;
    }

    //todo: come fare questo?
    @Override
    public ControllerMessage pick(List<Integer> choices) {
        return null;
    }

}
