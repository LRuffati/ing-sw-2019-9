package network.socket.messages;

public class PingRequest implements Response {
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
