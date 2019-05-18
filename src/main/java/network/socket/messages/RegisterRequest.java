package network.socket.messages;

import java.rmi.RemoteException;

public class RegisterRequest implements Request {
    public final String username;
    public final String color;

    public RegisterRequest(String username, String color){
        this.username = username;
        this.color = color;
    }

    @Override
    public Response handle(RequestHandler handler) throws RemoteException {
        return handler.handle(this);
    }
}
