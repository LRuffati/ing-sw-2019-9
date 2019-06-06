package network.socket.messages.notify;

import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnTimer implements Response {

    public final int timeToWait;

    public OnTimer(int ms) {
        this.timeToWait = ms;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
