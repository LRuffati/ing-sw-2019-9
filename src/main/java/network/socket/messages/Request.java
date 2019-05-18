package network.socket.messages;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Request extends Serializable {
    Response handle(RequestHandler handler) throws RemoteException;
}
