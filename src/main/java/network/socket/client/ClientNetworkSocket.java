package network.socket.client;

import network.ClientInterface;
import network.socket.messages.*;

import java.rmi.RemoteException;

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

    public void run() throws RemoteException {
        run(true, "");
    }

    public void run(boolean newConnection, String token) throws RemoteException {
        startReceiver();
        if(newConnection)
            register();
        else
            reconnect(token);
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
    public int close() {
        client.request(new CloseRequest(ClientContext.get().getToken()));
        return 0;
    }

    @Override
    public void register(){
        client.request(new RegisterRequest("a", "blue"));
        sync();
        System.out.println("token\n" + ClientContext.get().getToken());
    }

    @Override
    public boolean reconnect(String token) throws RemoteException {
        client.request(new ReconnectRequest(token));
        sync();
        System.out.println(ClientContext.get().getReconnected());
        return ClientContext.get().getReconnected();
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
        System.out.println("response");
        desync();
        System.out.println("desync");
    }

    @Override
    public void handle(ReconnectResponse response) {
        ClientContext.get().setReconnected(response.result);
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
