package network.socket.messages.notify;

import network.Player;
import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnConnection implements Response {

    public final Player playerConnected;
    public final boolean connected;

    public OnConnection(Player player, boolean connected) {
        this.playerConnected = player;
        this.connected = connected;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
