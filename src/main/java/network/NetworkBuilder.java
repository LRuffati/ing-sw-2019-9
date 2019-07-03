package network;

import gamemanager.ParserConfiguration;
import network.rmi.RMIServerLauncher;
import network.socket.SocketServerLauncher;
import controller.MainController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class initializes the network layer.
 * it creates the rmi and socket classes and then it sets up the timer used for checking if a player logged out
 */
public class NetworkBuilder {
    public NetworkBuilder(){}
    /**
     * Initialize rmi and socket connections
     */
    public static void NetworkStarter(MainController controller) {
        String host;

        Database.get().setMainController(controller);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserire indirizzo IP del server:\t");
        host = scanner.nextLine();
        try {
            RMIServerLauncher.RMILauncher(host, ParserConfiguration.parseInt("RMIPort"));
            new SocketServerLauncher(ParserConfiguration.parseInt("SocketPort"));
        }
        catch (IOException ex) {
            Logger.getLogger(NetworkBuilder.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        startPing();
    }

    private static void startPing(){
        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                Set<String> tokens = Database.get().getConnectedTokens();
                for (String token : tokens) {
                    ServerInterface network = Database.get().getNetworkByToken(token);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                //System.out.println(Database.get().getUserByToken(token).getUsername());
                                network.ping();
                            } catch (RemoteException | IllegalArgumentException e) {
                                e.printStackTrace();
                                Database.get().logout(token);
                            }
                            Thread.currentThread().interrupt();
                        }
                    };
                    thread.start();

                }

            }
        };

        Timer timer  = new Timer("pingTimer");
        timer.scheduleAtFixedRate(repeatedTask, 1000, 500);
    }
}
