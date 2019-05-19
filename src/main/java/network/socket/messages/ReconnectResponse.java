package network.socket.messages;

public class ReconnectResponse implements Response {
    public final boolean result;

    public ReconnectResponse(boolean result){
        this.result = result;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
