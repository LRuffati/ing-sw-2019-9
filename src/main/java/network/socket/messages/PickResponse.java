package network.socket.messages;

import controllerresults.ActionResultType;
import genericitems.Tuple;

public class PickResponse implements Response {

    public final int type;
    public final Tuple<ActionResultType, String> result;

    public PickResponse(int type, Tuple<ActionResultType, String> result){
        this.type = type;
        this.result = result;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
