package network.socket.messages;

public class RegisterRequest implements Request {
    public final String username;
    public final String color;

    public RegisterRequest(String username, String color){
        this.username = username;
        this.color = color;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
