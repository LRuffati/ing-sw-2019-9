package network.socket;

import network.socket.client.Client;
import network.socket.client.ClientNetworkSocket;

import java.io.IOException;
import java.util.Scanner;

public class SocketClientLauncher {
    public static void main(String[] args) throws IOException {
        System.out.println("\n\nClient Socket\n");


        Scanner reader = new Scanner(System.in);
        System.out.print("indirizzo: ");
        String host = reader.nextLine();
        System.out.print("porta: ");
        int port = reader.nextInt();

        if(host.equals(""))
            host = "localhost";
        if(port == 0)
            port = 8000;

        Client client = new Client(host, port);
        client.init();
        ClientNetworkSocket controller = new ClientNetworkSocket(client);
        controller.run();

        int num = 10;
        while(num>=0) {
            Scanner scanner = new Scanner(System.in);
            num = scanner.nextInt();
            if(num >= 0) {
                System.out.println("Uscito il num\t" + controller.mirror(num));
            }
            else {
                System.out.println("END!!\t" + controller.close(num));

            }
        }
        client.close();
    }

}
