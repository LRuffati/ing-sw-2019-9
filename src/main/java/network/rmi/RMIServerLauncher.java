package network.rmi;

import controller.InitGame;
import gamemanager.ParserConfiguration;
import network.rmi.server.ServerNetworkRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Demo class that starts a Server RMI
 * The connection made with localhost (127.0.0.1) host and a custom port
 *
 * The Server waits until an integer is read, then reply to the request.
 */
public class RMIServerLauncher {
    public RMIServerLauncher(int port){
        try{
            ServerNetworkRMI controller = new ServerNetworkRMI();
            System.out.println(">>> Controller exported");

            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("//localhost:" + port + "/controller", controller);

        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public RMIServerLauncher(){
        this(1099);
    }


    public static ServerNetworkRMI RMILauncher(int port){
        try{
            ServerNetworkRMI controller = new ServerNetworkRMI();
            System.out.println(">>> Controller exported");

            String host = ParserConfiguration.parse("RMIBinding");
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind(host, controller);

            startPing(controller);
            return controller;
        }
        catch (RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void startPing(ServerNetworkRMI controller){
        TimerTask repeatedTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if(controller instanceof ServerNetworkRMI)
                        controller.sendPing();
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        };

        Timer timer  = new Timer("pingTimer");
        timer.scheduleAtFixedRate(repeatedTask, 0, 100);
    }

    public static void main(String[] args) {
        System.out.println("\nServer RMI\n");

        Scanner reader = new Scanner(System.in);
        System.out.print("Porta: ");
        int porta = reader.nextInt();

        if(porta == 0)
            porta = 1099;

        new RMIServerLauncher(porta);
    }
}
