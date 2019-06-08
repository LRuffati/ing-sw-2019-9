package network.socket.messages;

import testcontroller.ChoiceBoard;

public class PickResponse implements Response {

    public final ChoiceBoard result;

    public PickResponse(ChoiceBoard result){
        this.result = result;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
