package network.socket.messages;

import viewclasses.GameMapView;

public class GetMapResponse implements Response {
    public final GameMapView gameMapView;

    public GetMapResponse(GameMapView gameMapView){
        this.gameMapView = gameMapView;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
