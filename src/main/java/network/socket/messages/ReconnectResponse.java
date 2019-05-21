package network.socket.messages;

public class ReconnectResponse implements Response {
    public final boolean result;
    public final String token;

    public ReconnectResponse(boolean result, String token){
        this.result = result;
        this.token = token;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
