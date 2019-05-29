package network.socket.messages;

public class ReconnectResponse implements Response {
    public final boolean result;
    public final boolean wrongUsername;
    public final boolean wrongColor;
    public final String token;

    public ReconnectResponse(String token){
        this.token = token;
        this.wrongUsername = false;
        this.wrongColor = false;
        this.result = true;
    }
    public ReconnectResponse(boolean wrongUsername) {
        this.token = "";
        this.wrongUsername = wrongUsername;
        this.wrongColor = false;
        this.result = false;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
