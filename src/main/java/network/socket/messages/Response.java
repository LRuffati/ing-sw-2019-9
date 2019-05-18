package network.socket.messages;

import java.io.Serializable;

public interface Response extends Serializable {
    void handle(ResponseHandler handler);
}
