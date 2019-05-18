package network.socket.messages;

import network.socket.client.ClientNetworkSocket;

import java.rmi.RemoteException;

public class RegisterRequest implements Request {
    //public final ClientNetworkSocket client;
    public final String username;
    public final String color;

    public RegisterRequest(ClientNetworkSocket client, String username, String color){
        //TODO: delete ClientNetworkSocket??
        //this.client = client;
        this.username = username;
        this.color = color;
    }

    @Override
    public Response handle(RequestHandler handler) throws RemoteException {
        return handler.handle(this);
    }
}
