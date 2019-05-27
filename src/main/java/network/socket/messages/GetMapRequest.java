package network.socket.messages;

public class GetMapRequest implements Request {
    public final String gameMapId;

    public GetMapRequest(String gameMapId) {
        this.gameMapId = gameMapId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
