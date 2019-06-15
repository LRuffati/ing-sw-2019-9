package network.socket.messages.notify;

import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnStarting implements Response {

    public final String mapName;

    public OnStarting(String map) {
        this.mapName = map;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
