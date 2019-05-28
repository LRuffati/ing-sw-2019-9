package network.socket.messages;

import controllerresults.ControllerActionResultClient;

public class PickResponse implements Response {

    public final int type;
    public final ControllerActionResultClient result;

    public PickResponse(int type, ControllerActionResultClient result){
        this.type = type;
        this.result = result;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
