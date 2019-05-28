package network.socket.messages;

public class ShowOptionsRequest implements Request{

    public final String token;

    public final int type;
    public final String chooserId;

    public ShowOptionsRequest(String token, int type, String chooserId){
        this.token = token;
        this.type = type;
        this.chooserId = chooserId;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
