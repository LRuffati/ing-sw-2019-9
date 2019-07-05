package network.socket.messages;

/**
 * This interface contains all the Request that the Server can receive
 */
public interface RequestHandler {
    /**
     *  Message sent to request a logout operation in the Server
     */
    Response handle(CloseRequest request);

    /**
     *  Message sent to request a mirror operation in the Server
     */
    Response handle(MirrorRequest request);

    /**
     *  Message sent to request a login operation in the Server
     */
    Response handle(RegisterRequest request);
    /**
     *  Message sent to request a login (reconnection) operation in the Server
     */
    Response handle(ReconnectRequest request);

    /**
     *  Message sent to request a pick operation in the Server
     */
    Response handle(PickRequest request);

    /**
     *  Message sent to request map from the Server
     */
    Response handle(GetMapRequest request);

    /**
     *  Message sent to check for updates
     */
    Response handle(PollRequest request);

    /**
     *  Message sent notify his presence
     */
    Response handle(PingResponse response);

    /**
     *  Message sent to vote for the gameMode
     */
    Response handle(ModeRequest request);
}
