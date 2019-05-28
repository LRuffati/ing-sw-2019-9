package network.socket.messages;

public class RegisterRequest implements Request {
    public final String username;
    public final String password;
    public final String color;

    public RegisterRequest(String username, String password, String color){
        this.username = username;
        this.password = password;
        this.color = color;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
