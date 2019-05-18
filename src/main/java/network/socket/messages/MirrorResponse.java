package network.socket.messages;

public class MirrorResponse implements Response {
    public final int num;

    public MirrorResponse(int num) {
        this.num = num;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
