package network;

import controller.GameMode;
import gamemanager.ParserConfiguration;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.rmi.RMIServerLauncher;
import network.rmi.client.ClientNetworkRMI;
import network.rmi.server.ServerRMIInterface;
import network.socket.SocketServerLauncher;
import network.socket.client.Client;
import network.socket.client.ClientNetworkSocket;
import network.exception.InvalidLoginException;

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
    private static void runServers() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserire indirizzo IP del server:\t");
        String host = scanner.nextLine();

        int port = ParserConfiguration.parseInt("RMIPort");
        RMIServerLauncher.RMILauncher(host, port);
        new SocketServerLauncher(ParserConfiguration.parseInt("SocketPort"));

        int i = 0;
        while(i < 100){
            i++;
            scanner.nextInt();
            System.out.println("Numero di utenti\t" + Database.get().getConnectedTokens().size());
            List<ServerInterface> interfaces = Database.get().getConnectedTokens().stream().map(x -> Database.get().getNetworkByToken(x)).collect(Collectors.toList());
            for(ServerInterface net : interfaces){
                net.sendUpdate("ciao");
            }
        }
    }

    private static void runSocket() throws IOException{
        System.out.println("\nClient Socket\n");
        String host  = "localhost";
        Client client = new Client(host, ParserConfiguration.parseInt("SocketPort"));
        client.init();
        ClientNetworkSocket controller = new ClientNetworkSocket(client, null);
        //controller.run();

        register(controller);
        run(controller);

        //client.close();
    }

    private static void runRMI() throws RemoteException, InvalidLoginException, NotBoundException {
        System.out.println("\nClient RMI\n");
        Registry registry = LocateRegistry.getRegistry();

        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");

        String host = "localhost";
        String hostName = ParserConfiguration.parse("RMIBinding");
        int port = ParserConfiguration.parseInt("RMIPort");
        //String lookup = String.format("//%s:%d/%s", host, port, hostName);
        String lookup = hostName;
        ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(lookup);

        ClientNetworkRMI client = new ClientNetworkRMI(controller, null);
        //client.run();

        register(client);
        run(client);
    }


    private static void runSocketReconnect() throws IOException{
        System.out.println("\nClient Socket Reconnect\n");
        Client client = new Client("localhost", ParserConfiguration.parseInt("SocketPort"));
        client.init();
        ClientNetworkSocket controller = new ClientNetworkSocket(client, null);

        //controller.run();

        reconnect(controller);
        run(controller);

        //client.close();
    }

    private static void runRMIReconnect() throws RemoteException, InvalidLoginException, NotBoundException {
        System.out.println("\nClient RMI Reconnect\n");
        Registry registry = LocateRegistry.getRegistry();

        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");

        String host = ParserConfiguration.parse("RMIBinding");
        int port = ParserConfiguration.parseInt("RMIPort");
        String lookup = String.format("//%s:%d/controller", host, port);
        ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(lookup);

        ClientNetworkRMI client = new ClientNetworkRMI(controller, null);

        /*System.out.println("Insert Token");
        client.run(false, new Scanner(System.in).next());*/
        reconnect(client);

        run(client);
    }

    public static void register(ClientInterface clientInterface) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean res = false;
        do {
            System.out.print("Insert username:\t");
            String username = scanner.next();
            System.out.print("Insert password:\t");
            String password = scanner.next();
            System.out.print("Insert colour:\t");
            String colour = scanner.next();
            try {
                res = clientInterface.register(username, password, colour);
            }
            catch (InvalidLoginException e){
                if(e.wrongUsername)
                    System.out.println("This username already exists");
                if(e.wrongColor)
                    System.out.println("This color already exists");
            }
        } while(!res);
    }

    private static void reconnect(ClientInterface clientInterface) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean res = false;
        do {
            System.out.print("Insert username:\t");
            String username = scanner.next();
            System.out.print("Insert password:\t");
            String password = scanner.next();
            try {
                Tuple3<Boolean, Boolean, Tuple<String, GameMode>> resu = clientInterface.reconnect(username, password);
                res = resu.x;
            }
            catch (InvalidLoginException e){
                if(e.wrongUsername)
                    System.out.println("This username already exists");
            }
        } while(!res);
    }

    public static void run(ClientInterface clientInterface) throws RemoteException{
        int num = 10;
        while(num>=0) {
            System.out.print("Next int:\t");
            Scanner scanner = new Scanner(System.in);
            num = scanner.nextInt();
            if(num >= 0) {
                System.out.println("Uscito il num\t" + clientInterface.mirror(num));
                if(num == 0) {
                    clientInterface.close();
                }

            }
            else {
                System.out.println("END!!\t" + clientInterface.close());

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
        if(n == 11)
            runSocketReconnect();
        if(n == 22)
            runRMIReconnect();
    }
}
