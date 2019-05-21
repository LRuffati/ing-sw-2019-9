package network.socket.server;

import network.Database;
import network.socket.messages.Request;
import network.socket.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to send data to the Client via socket
 */
public class ClientHandler implements Runnable{
    private Logger logger;
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private boolean stop;

    private final ServerNetworkSocket controller;

    public ClientHandler(Socket socket) throws IOException {
        logger = Logger.getLogger(ClientHandler.class.getName());
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());

        this.controller = new ServerNetworkSocket(this);

        stop = false;
    }

    public void respond(Response response){
        try{
            out.writeObject(response);
            //TODO: giusto farlo qui?
            out.flush();
            //out.reset();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "IO - " + e.getMessage());
        }
    }


    @Override
    public void run() {
        try {
            do {
                Response response = ((Request) in.readObject()).handle(controller);
                if (response != null) {
                    respond(response);
                }
            } while (!stop);
        } catch (SocketException e) {
            //TODO: notify controller too?
            Database.get().logout(controller);
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        close();
    }

    public void stop() {
        stop = true;
    }

    private void close(){
        String err = "Errors in closing - ";
        stop = true;
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, err + e.getMessage());
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, err + e.getMessage());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, err + e.getMessage());
        }
    }
}
