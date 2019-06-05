package network;

import controller.InitGame;
import gamemanager.ParserConfiguration;
import network.rmi.RMIServerLauncher;
import network.socket.SocketServerLauncher;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkBuilder {
    //public NetworkBuilder(MainController controller) {
    public NetworkBuilder(InitGame controller) {
        String host;
        int port;

        Database.get().setMainController(controller);

        host = "localhost";
        //host = ParserConfiguration.parse("RMIBinding");
        port = ParserConfiguration.parseInt("RMIPort");
        try {
            RMIServerLauncher.RMILauncher(host, port);
            new SocketServerLauncher(ParserConfiguration.parseInt("SocketPort"));
        }
        catch (IOException ex) {
            Logger.getLogger(NetworkBuilder.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}
