package network.socket.messages;

public class GetMapRequest implements Request {

    public GetMapRequest() { }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
