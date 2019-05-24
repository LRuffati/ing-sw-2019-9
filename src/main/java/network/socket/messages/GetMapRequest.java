package network.socket.messages;

public class GetMapRequest implements Request {
    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
