package network.socket.messages.notify;

import network.Player;
import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnConnection implements Response {

    public final Player playerConnected;
    public final boolean connected;
    public final int numOfPlayer;
    public final boolean lostTurn;

    public OnConnection(Player player, boolean connected, int numOfPlayer, boolean lostTurn) {
        this.playerConnected = player;
        this.connected = connected;
        this.numOfPlayer = numOfPlayer;
        this.lostTurn = lostTurn;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
