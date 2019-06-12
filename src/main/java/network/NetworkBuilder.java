package network;

import controller.MainController;
import gamemanager.ParserConfiguration;
import network.rmi.RMIServerLauncher;
import network.rmi.server.ServerNetworkRMI;
import network.socket.SocketServerLauncher;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkBuilder {
    public NetworkBuilder(MainController controller) {
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
                try {
                    for(String token : Database.get().getConnectedTokens()) {
                        Database.get().getNetworkByToken(token).ping();
                    }
                }
                catch (RemoteException e){
                    //empty
                }
            }
        };

        Timer timer  = new Timer("pingTimer");
        timer.scheduleAtFixedRate(repeatedTask, 1000, 100);
    }
}
