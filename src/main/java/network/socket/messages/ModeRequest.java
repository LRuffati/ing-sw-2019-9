package network.socket.messages;

public class ModeRequest implements Request {
    public final boolean normalMode;

    public ModeRequest(boolean normalMode) {
        this.normalMode = normalMode;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
