package network.socket.messages;

public class CloseResponse implements Response {
    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
