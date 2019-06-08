package network;

import controller.MainController;
import gamemanager.ParserConfiguration;
import network.rmi.RMIServerLauncher;
import network.socket.SocketServerLauncher;

import java.io.IOException;
import java.util.Scanner;
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
    }
}
