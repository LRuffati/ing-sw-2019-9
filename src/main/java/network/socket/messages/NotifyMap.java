package network.socket.messages;

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
