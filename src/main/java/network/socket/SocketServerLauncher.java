package network.socket;


import network.Database;
import network.socket.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class SocketServerLauncher {
    private static ServerSocket serverSocket;
    private static ExecutorService pool;
    private static Thread conn;

    public SocketServerLauncher(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        System.out.println( ">>> Listening on " + port);
        receiveConnections(this);
        //run();
    }

    public void run() throws IOException, RemoteException {
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
