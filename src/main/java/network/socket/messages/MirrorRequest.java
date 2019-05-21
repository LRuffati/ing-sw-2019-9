package network.socket.messages;

public class MirrorRequest implements Request{

    public final int num;
    public final String token;

    public MirrorRequest(String token, int n){
        this.token = token;
        this.num = n;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
