package network.socket.messages;

import controller.controllermessage.ControllerMessage;

public class PickResponse implements Response {

    public final ControllerMessage result;

    public PickResponse(ControllerMessage result){
        this.result = result;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
