package network.socket.messages;

/**
 * This interface contains all the Request that the Server can receive
 */
public interface RequestHandler {
    Response handle(CloseRequest request);

    Response handle(MirrorRequest request);

    Response handle(RegisterRequest request);
    Response handle(ReconnectRequest request);

    Response handle(PickRequest request);
    Response handle(ShowOptionsRequest request);

    Response handle(GetMapRequest request);
}
