package network.socket.messages;

public class ShowOptionsRequest implements Request{

    public final int type;
    public final String chooserId;

    public ShowOptionsRequest(int type, String chooserId){
        this.type = type;
        this.chooserId = chooserId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
