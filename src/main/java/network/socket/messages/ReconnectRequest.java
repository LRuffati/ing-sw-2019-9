package network.socket.messages;

public class ReconnectRequest implements Request {
    public final String token;

    public ReconnectRequest(String token){
        this.token = token;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
