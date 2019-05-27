package network.rmi.client;

import network.ClientInterface;
import network.ServerInterface;

import java.rmi.Remote;

/**
 * Methods called by ServerNetworkRMI.
 * All the method of ServerRMIInterface are executed directly on the clientSide, so ServerInterface must be implemented by the Client
 */
public interface ClientNetworkRMIInterface extends Remote, ClientInterface, ServerInterface {
}
