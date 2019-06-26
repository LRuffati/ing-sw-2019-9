package network.socket.messages.notify;

import controller.GameMode;
import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnStarting implements Response {

    public final String mapName;
    public final GameMode gameMode;

    public OnStarting(String map, GameMode gameMode) {
        this.mapName = map;
        this.gameMode = gameMode;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
