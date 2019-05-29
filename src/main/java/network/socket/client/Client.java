package network.socket.client;

import controllerclient.ClientControllerNetworkInterface;
import network.exception.NextResponseException;
import network.socket.messages.Request;
import network.socket.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to send data to the Server via socket
 */
public class Client {
    private Logger logger = Logger.getLogger(Client.class.getName());

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        this.socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public Response nextResponse() {
        try {
            return ((Response) in.readObject());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new NextResponseException("Wrong deserialization: " + e.getMessage());
        }

        return null;
    }

    public void request(Request request) {
        try {
            out.writeObject(request);
            out.reset();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "exception on network: " + e.getMessage());
        }
    }
}