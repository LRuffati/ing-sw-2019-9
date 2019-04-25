package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class SocketServer {
    private ServerSocket sc;
    private int port;
    private SocketAddress ad;

    public void startServer() throws IOException{
        sc = new ServerSocket();
        sc.accept();
    }
}
