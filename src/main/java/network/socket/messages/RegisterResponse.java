package network.socket.messages;

public class RegisterResponse implements Response {
    public final String token;

    public RegisterResponse(String token){
        this.token = token;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
