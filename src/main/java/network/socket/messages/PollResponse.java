package network.socket.messages;

import controller.controllermessage.ControllerMessage;

public class PollResponse implements Response {
    public final ControllerMessage controllerMessage;

    public PollResponse(ControllerMessage controllerMessage) {
        this.controllerMessage = controllerMessage;
    }
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
