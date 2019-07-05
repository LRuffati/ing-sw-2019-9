package network.socket.messages;

import network.socket.messages.notify.*;

/**
 * This interface contains all the Response that the Client can receive
 */
public interface ResponseHandler {
    /**
     * Message sent to send an Exception to the client
     */
    void handle(ExceptionResponse response);

    /**
     * Message sent to send a response to the closeRequest
     */
    void handle(CloseResponse response);
    /**
     * Message sent to send a response to the MirrorRequest
     */
    void handle(MirrorResponse response);

    /**
     * Message sent to notify the result of the login operation
     */
    void handle(RegisterResponse response);
    /**
     * Message sent to notify the result of the login (reconnect) operation
     */
    void handle(ReconnectResponse response);

    /**
     * Message sent to send a text message to the client
     */
    void handle(TextResponse response);

    /**
     * Message sent to notify the result of the pickRequest
     */
    void handle(PickResponse response);

    /**
     * Message sent to send the map requested by the client
     */
    void handle(GetMapResponse response);

    /**
     * Message sent to notify the client of the updates
     */
    void handle(PollResponse response);
    /**
     * Message sent to send a new map to the client
     */
    void handle(NotifyMap response);

    /**
     * Message sent to send a ping request
     */
    void handle(PingRequest request);

    /**
     * Message sent to notify a new Timer
     */
    void handle(OnTimer response);
    /**
     * Message sent to notify a new Connection/Disconnection
     */
    void handle(OnConnection response);
    /**
     * Message sent to notify the starting of the game
     */
    void handle(OnStarting response);
    /**
     * Message sent to notify the winner of the game
     */
    void handle(OnWinner response);
}
