package network.socket.messages;

public class RegisterResponse implements Response {
    public final String token;
    public final boolean result;
    public final boolean wrongUsername;
    public final boolean wrongColor;

    public RegisterResponse(String token){
        this.token = token;
        this.wrongUsername = false;
        this.wrongColor = false;
        this.result = true;
    }
    public RegisterResponse(boolean wrongUsername, boolean wrongColor) {
        this.token = "";
        this.wrongUsername = wrongUsername;
        this.wrongColor = wrongColor;
        this.result = false;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
