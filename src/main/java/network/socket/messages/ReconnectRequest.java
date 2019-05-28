package network.socket.messages;

public class ReconnectRequest implements Request {
    public final String username;
    public final String password;

    public ReconnectRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
