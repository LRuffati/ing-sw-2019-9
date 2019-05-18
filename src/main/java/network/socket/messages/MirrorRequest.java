package network.socket.messages;

public class MirrorRequest implements Request{

    public final int num;

    public MirrorRequest(int n){
        this.num = n;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
