package controller.controllerclient;

import controller.ChoiceBoard;
import controller.Message;
import controller.controllermessage.ControllerMessage;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.io.Serializable;
import java.util.List;

public class ControllerMessageClient implements ControllerMessage, Serializable {

    public final String id;

    private SlaveControllerState type;
    private ChoiceBoard choiceBoard;
    private List<String> changes;
    private GameMapView sandbox;


    public ControllerMessageClient(ControllerMessage controllerMessage, String id) {
        this.id = id;

        this.type = controllerMessage.type();
        this.choiceBoard = controllerMessage.genView();
        this.sandbox = controllerMessage.sandboxView();

        if(controllerMessage.getMessage() == null)
            this.changes = List.of();
        else
            this.changes = controllerMessage.getMessage().getChanges();
    }

    public ControllerMessageClient() {
        this.id = null;

        this.type = SlaveControllerState.WAIT;
        this.choiceBoard = null;
        this.changes = List.of();
        this.sandbox = null;
    }

    public ControllerMessageClient(ControllerMessage controllerMessage, String id, SlaveControllerState state) {
        this.id = id;

        this.type = state;
        this.choiceBoard = controllerMessage.genView();
        this.sandbox = controllerMessage.sandboxView();
        this.changes = List.of();
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
        return new Message() {
            @Override
            public List<String> getChanges() {
                return changes;
            }
        };
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