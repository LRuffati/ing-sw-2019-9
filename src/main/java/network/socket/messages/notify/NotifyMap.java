package network.socket.messages.notify;

import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;
import viewclasses.GameMapView;

public class NotifyMap implements Response {

    public final GameMapView gameMap;
    public NotifyMap(GameMapView gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
