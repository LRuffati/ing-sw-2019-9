package controller.controllerclient;

import controller.ChoiceBoard;
import controller.Message;
import controller.controllermessage.ControllerMessage;
import controller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.io.Serializable;
import java.util.List;

/**
 * This class contains an implementation of {@link ControllerMessage ControllerMessage} that can be sent through the network layer
 */
public class ControllerMessageClient implements ControllerMessage, Serializable {

    public final String id;

    private SlaveControllerState type;
    private ChoiceBoard choiceBoard;
    private List<String> changes;
    private GameMapView sandbox;

    /**
     * Basic contructor, it generates a new ControllerMessageClient given the original ControllerMessage and an identifier
     */
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

    /**
     * This constructor generates an empty ControllerMessage of type WAIT
     */
    public ControllerMessageClient() {
        this.id = null;

        this.type = SlaveControllerState.WAIT;
        this.choiceBoard = null;
        this.changes = List.of();
        this.sandbox = null;
    }

    /**
     * This contructor generates a new ControllerMessage of a custom type
     */
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
        return null;
    }


}
