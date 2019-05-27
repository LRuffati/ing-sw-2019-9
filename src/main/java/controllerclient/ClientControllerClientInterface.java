package controllerclient;

import controllerresults.ControllerActionResultClient;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface implemented by ClientController. It contains all the method that can be used by a View
 */
public interface ClientControllerClientInterface {
    void pick(ControllerActionResultClient elem, List<Integer> choices) throws RemoteException;
    void restartSelection() throws RemoteException;
    void rollback() throws RemoteException;
}
