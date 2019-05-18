package network.socket.client;

import network.ClientInterface;
import network.socket.messages.*;

/**
 * This class handles all the methods called by the client (implemented in ClientInterface)
 * and the Requests called by the server (implemented in ResponseHandler)
 */
public class ClientNetworkSocket implements ResponseHandler, ClientInterface {
    private final Client client;
    private Thread receiver;

    public ClientNetworkSocket(Client client){
        this.client = client;
    }

    private void sync(){
        synchronized (client) {
            try {
                client.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
    private void desync() {
        synchronized (client) {
            client.notifyAll();
        }
    }

    public void run() {
        register();
        startReceiver();
        System.out.println("Ready to receive");
    }

    public void startReceiver() {
        // start a receiver thread
        receiver = new Thread(
                () -> {
                    Response response;
                    do {
                        response = client.nextResponse();
                        if (response != null) {
                            response.handle(this);
                        }
                    } while (response != null);
                }
        );
        receiver.start();
    }

    //ClientInterface methods

    @Override
    public int mirror(int num) {
        client.request(new MirrorRequest(num));
        sync();
        return ClientContext.get().getMirror();
    }

    @Override
    public int close(int num) {
        client.request(new CloseRequest(num));
        return 0;
    }

    @Override
    public void register(){
        client.request(new RegisterRequest("a", "blue"));
        sync();
        ClientContext.get().getToken();
    }


    //ClientHandler methods

    @Override
    public void handle(TextResponse response) {
        ClientContext.get().setTextMessage(response.toString());
        System.out.println("TEXT MESSAGE\t" + response.toString());
    }

    @Override
    public void handle(RegisterResponse response) {
        ClientContext.get().setToken(response.token);
        desync();
    }

    @Override
    public void handle(CloseResponse response) {
        System.out.println("Permesso di uscita");
        receiver.interrupt();
    }

    @Override
    public void handle(MirrorResponse response) {
        ClientContext.get().setMirror(response.num);
        desync();
    }
}
