package network.socket.server;

import network.Database;
import network.socket.messages.Request;
import network.socket.messages.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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

    synchronized void respond(Response response){
        try{
            out.writeObject(response);
            out.flush();
            out.reset();
        }
        catch (SocketException e) {
            System.out.println("Hard quit");
            Database.get().logout(controller);
        }
        catch (IOException e) {
            e.printStackTrace();
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
        } catch (SocketException | SocketTimeoutException e) {
            System.out.println("Soft quit");
            //TODO: notify controller too?
            e.printStackTrace();
            Database.get().logout(controller);
            close();
        }
        catch (OptionalDataException e) {
            e.printStackTrace();
            System.out.println(e.eof+"\t\t"+e.length);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getClass().getSimpleName() + " - " + e.getMessage());
        }
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
                logger.log(Level.SEVERE, "In:\t" + err + e.getMessage());
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Out:\t" + err + e.getMessage());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Socket:\t" + err + e.getMessage());
        }
    }
}
