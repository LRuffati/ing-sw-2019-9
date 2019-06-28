package network.socket.messages;

import controller.GameMode;
import genericitems.Tuple;

public class ReconnectResponse implements Response {
    public final boolean result;
    public final boolean wrongUsername;
    public final boolean wrongColor;
    public final String token;
    public final Tuple<String, GameMode> tuple;

    public final boolean isStarted;

    public ReconnectResponse(String token, boolean isStarted, Tuple<String, GameMode> tuple){
        this.token = token;
        this.wrongUsername = false;
        this.wrongColor = false;
        this.result = true;

        this.isStarted = isStarted;
        this.tuple = tuple;
    }
    public ReconnectResponse(boolean wrongUsername) {
        this.token = "";
        this.wrongUsername = wrongUsername;
        this.wrongColor = false;
        this.result = false;

        this.isStarted = false;
        this.tuple = null;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
