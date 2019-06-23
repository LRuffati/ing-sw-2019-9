package network.socket.messages;

public class ReconnectResponse implements Response {
    public final boolean result;
    public final boolean wrongUsername;
    public final boolean wrongColor;
    public final String token;

    public final boolean isStarted;

    public ReconnectResponse(String token, boolean isStarted){
        this.token = token;
        this.wrongUsername = false;
        this.wrongColor = false;
        this.result = true;

        this.isStarted = isStarted;
    }
    public ReconnectResponse(boolean wrongUsername) {
        this.token = "";
        this.wrongUsername = wrongUsername;
        this.wrongColor = false;
        this.result = false;

        this.isStarted = false;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
