package network.rmi;

import network.rmi.client.ClientNetworkRMI;
import network.rmi.server.ServerRMIInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Demo class that starts a Client RMI
 * The connection made with localhost (127.0.0.1) host and port 1099
 *
 * The program consist in sending integers to the Server, reading the response and printing them.
 */
public class RMIClientLauncher {

    public static void main(String[] args) throws Exception {
        System.out.println("\nClient RMI\n");

        Registry registry = LocateRegistry.getRegistry();

        for (String name : registry.list()) {
            System.out.println("Registry bindings: " + name);
        }
        System.out.println("\n");

        // gets a reference for the remote controller
        //TODO: which controller??
        ServerRMIInterface controller = (ServerRMIInterface) registry.lookup("//localhost:1099/controller");

        ClientNetworkRMI client = new ClientNetworkRMI(controller);
        client.run();

        int num = 10;
        while(num>=0) {
            Scanner scanner = new Scanner(System.in);
            num = scanner.nextInt();
            if(num >= 0) {
                int n = controller.mirror(num);
                System.out.println("Mirrored\t" + n);
            }
            else {
                controller.close(client);
                System.out.println("Permesso di uscita");
            }
        }
        //TODO: how to do that?
        // creates and launches the view
        //new View(controller).run();
    }
}
