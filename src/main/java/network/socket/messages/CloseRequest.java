package network.socket.messages;

public class CloseRequest implements Request {

    public final String token;

    public CloseRequest(String token){
        this.token = token;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
