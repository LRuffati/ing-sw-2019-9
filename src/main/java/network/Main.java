package network;

import gamemanager.ParserConfiguration;
import network.rmi.RMIServerLauncher;
import network.rmi.client.ClientNetworkRMI;
import network.rmi.server.ServerRMIInterface;
import network.socket.SocketServerLauncher;
import network.socket.client.Client;
import network.socket.client.ClientNetworkSocket;
import rmi.exceptions.InvalidLoginException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Demo class that allow the user to start a server or a client (rmi or socket).
 * Clients sends an integer and receive the same integer back.
 * The server can send a message to every Client connected.
 */
public class Main {
    public static void runServers() throws IOException {
        String host = "localhost";
        RMIServerLauncher.RMILauncher(host, ParserConfiguration.parseInt("RMIPort"));
        new SocketServerLauncher(ParserConfiguration.parseInt("SocketPort"));

        Scanner scanner = new Scanner(System.in);
        int i = 0;
        while(i < 100){
            i++;
            scanner.nextInt();
            List<ServerInterface> interfaces = Database.get().getTokens().stream().map(x -> Database.get().getNetworkByToken(x)).collect(Collectors.toList());
            for(ServerInterface net : interfaces){
                net.sendUpdate("ciao");
            }
        }
    }

    public static void runSocket() throws IOException{
        System.out.println("\nClient Socket\n");
        Client client = new Client("localhost", ParserConfiguration.parseInt("SocketPort"));
        client.init();
        ClientNetworkSocket controller = new ClientNetworkSocket(client);
        controller.run();

        run(controller);

        client.close();
    }

    public static void runRMI() throws RemoteException, InvalidLoginException, NotBoundException {
        System.out.println("\nClient RMI\n");
        Registry registry = LocateRegistry.getRegistry();

        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");
        String host = "localhost";
        String lookup = String.format("//%s:%d/controller", host, ParserConfiguration.parseInt("RMIPort"));
        ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(lookup);

        ClientNetworkRMI client = new ClientNetworkRMI(controller);
        client.run();

        run(client);
    }

    public static void run(ClientInterface clientInterface) throws RemoteException{
        int num = 10;
        while(num>=0) {
            Scanner scanner = new Scanner(System.in);
            num = scanner.nextInt();
            if(num >= 0) {
                System.out.println("Uscito il num\t" + clientInterface.mirror(num));
            }
            else {
                System.out.println("END!!\t" + clientInterface.close(num));

            }
        }
    }



    public static void main(String[] str) throws IOException, InvalidLoginException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("0 per server\n1 per ClientSocket\n2 per ClientRMI");
        int n = scanner.nextInt();
        if(n == 0)
            runServers();
        if(n == 1)
            runSocket();
        if(n == 2)
            runRMI();
    }
}
