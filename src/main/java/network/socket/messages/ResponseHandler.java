package network.socket.messages;

/**
 * This interface contains all the Response that the Client can receive
 */
public interface ResponseHandler {
    void handle(CloseResponse response);

    void handle(MirrorResponse response);

    void handle(RegisterResponse response);
    void handle(ReconnectResponse response);

    void handle(TextResponse response);
}
