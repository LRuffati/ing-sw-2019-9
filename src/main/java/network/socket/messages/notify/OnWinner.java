package network.socket.messages.notify;

import network.socket.messages.Response;
import network.socket.messages.ResponseHandler;

public class OnWinner implements Response {

    public final String winner;
    public final int winnerPoints;
    public final int yourPoints;

    public OnWinner(String winner, int winnerPoints, int yourPoints) {
        this.winner = winner;
        this.winnerPoints = winnerPoints;
        this.yourPoints = yourPoints;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
