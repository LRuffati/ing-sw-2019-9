package network.socket;


import network.Database;
import network.socket.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;
/**
 * Demo class that starts a Socket Server
 * The connection made with localhost (127.0.0.1) host and a custom port
 *
 * The Server waits until an integer is read, then reply to the request.
 */
public class SocketServerLauncher {
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Thread conn;

    public SocketServerLauncher(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        System.out.println( ">>> Listening on " + port);
        receiveConnections(this);
    }

    public void run() throws IOException {
        System.in.read();
        for(String token : Database.get().getConnectedTokens())
            Database.get().getNetworkByToken(token).sendUpdate("ciao");
    }

    private void close() throws IOException {
        serverSocket.close();
    }

    private void receiveConnections(SocketServerLauncher socketServerLauncher) {
        // start a receiver thread
        conn = new Thread(
                () -> {
                    while(true) {
                        try {
                            Socket socket = serverSocket.accept();
                            System.out.println(">>> New connection " + socket.getRemoteSocketAddress());
                            //new clientHandler(socket);
                            //TODO: funziona senza pool?
                            pool.submit(new ClientHandler(socket));

                            if (socket.toString().equals("stringa improbabile")) break;
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    try {
                        conn.interrupt();
                        socketServerLauncher.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                }
        );
        conn.start();
    }


    public static void main(String[] args) throws IOException {
        System.out.println("\n\nServer Socket\n");

        Scanner reader = new Scanner(System.in);
        System.out.print("Porta: ");
        int porta = reader.nextInt();

        if(porta == 0)
            porta = 8000;

        SocketServerLauncher server = new SocketServerLauncher(porta);
    }
}
