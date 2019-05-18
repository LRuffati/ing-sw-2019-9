package network.socket.messages;

public class CloseRequest implements Request {

    public final int value;

    public CloseRequest(int value){
        this.value = value;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
