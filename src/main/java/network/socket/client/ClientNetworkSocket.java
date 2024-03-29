package network.socket.client;

import controller.GameMode;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.socket.messages.notify.*;
import controller.controllerclient.ClientControllerNetworkInterface;
import network.ClientInterface;
import network.exception.InvalidLoginException;
import network.socket.messages.*;

import java.io.IOException;
import java.util.List;

/**
 * This class handles all the methods called by the client (implemented in {@link ClientInterface ClientInterface})
 * and the Requests called by the server (implemented in {@link ResponseHandler Responsehandler})
 */
public class ClientNetworkSocket implements ResponseHandler, ClientInterface {
    private final Client client;
    private Thread receiver;

    private ClientControllerNetworkInterface clientController;


    public ClientNetworkSocket(Client client, ClientControllerNetworkInterface clientController){
        this.client = client;
        this.clientController = clientController;
        run();
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
        startReceiver();
        System.out.println("Ready to receive");
    }

    /**
     * This method continuously wait until a message is read, then it handle it
     */
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
        client.request(new MirrorRequest(ClientContext.get().getToken(), num));
        sync();
        return ClientContext.get().getMirror();
    }

    @Override
    public int close() {
        client.request(new CloseRequest(ClientContext.get().getToken()));
        return 0;
    }

    @Override
    public boolean register(String username, String password, String color) throws InvalidLoginException {
        client.request(new RegisterRequest(username, password, color));
        sync();
        ClientContext c = ClientContext.get();
        if(c.getConnectionResult()) {
            String token = ClientContext.get().getToken();
            System.out.println("token\n" + token);
            return true;
        }
        else {
            throw new InvalidLoginException("Invalid connection", c.getLoginException().x, c.getLoginException().y);
        }
    }

    @Override
    public Tuple3<Boolean, Boolean, Tuple<String, GameMode>> reconnect(String username, String password) throws InvalidLoginException {
        client.request(new ReconnectRequest(username, password));
        sync();
        ClientContext c = ClientContext.get();
        if(c.getConnectionResult()) {
            String token = ClientContext.get().getToken();
            System.out.println("Reconnected with: token\n" + token);
            return new Tuple3<>(true, ClientContext.get().getIsStarted(), ClientContext.get().getTupleReconnection());
        }
        else {
            throw new InvalidLoginException("Invalid reconnection", c.getLoginException().x, c.getLoginException().y);
        }
    }

    @Override
    public void modeRequest(boolean normalMode) {
        client.request(new ModeRequest(normalMode));
    }

    /**
     * @param chooserId Id of the chosen container
     * @param choice A list containing all the chosen index. Server will analyze if a List or an int is needed
     */
    @Override
    public void pick(String chooserId, List<Integer> choice){
        client.request(new PickRequest(chooserId, choice));
    }


    @Override
    public void getMap() {
        client.request(new GetMapRequest());
    }

    @Override
    public void poll() {
        client.request(new PollRequest());
    }

    //ClientHandler methods

    @Override
    public void handle(TextResponse response) {
        ClientContext.get().setTextMessage(response.toString());
        System.out.println("TEXT MESSAGE\t" + response.toString());
    }

    @Override
    public void handle(RegisterResponse response) {
        ClientContext.get().setConnectionResult(response.result);
        if(!response.result) ClientContext.get().setLoginException(response.wrongUsername, response.wrongColor);
        ClientContext.get().setToken(response.token);
        desync();
    }

    @Override
    public void handle(ReconnectResponse response) {
        ClientContext.get().setConnectionResult(response.result);
        if(!response.result) ClientContext.get().setLoginException(response.wrongUsername, response.wrongColor);
        ClientContext.get().setToken(response.token);
        ClientContext.get().setIsStarted(response.isStarted);
        ClientContext.get().setTupleReconnection(response.tuple);
        desync();
    }

    @Override
    public void handle(CloseResponse response) {
        System.out.println("Permesso di uscita");
        try {
            client.close();
            receiver.interrupt();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Finita la chiusura");
    }

    @Override
    public void handle(MirrorResponse response) {
        System.out.println(response.num);
        ClientContext.get().setMirror(response.num);
        desync();
    }


    @Override
    public void handle(PickResponse response) {
        clientController.onControllerMessage(response.result);
    }

    @Override
    public void handle(ExceptionResponse response) {
        //TODO: restituire il messaggio al controller
        //TODO: throw error message
        System.out.println(response.exception.getMessage());
    }

    @Override
    public void handle(GetMapResponse response) {
        clientController.updateMap(response.gameMapView);
    }


    @Override
    public void handle(PingRequest request) {
        client.request(new PingResponse());
        clientController.reset();
    }

    //ClientControllerNetworkInterface methods
    @Override
    public void handle(NotifyMap response) {
        clientController.updateMap(response.gameMap);
    }

    @Override
    public void handle(OnConnection response) {
        clientController.onConnection(response.playerConnected, response.connected, response.numOfPlayer, response.lostTurn);
    }

    @Override
    public void handle(OnStarting response) {
        clientController.onStarting(response.mapName, response.gameMode);
    }

    @Override
    public void handle(OnTimer response) {
        clientController.onTimer(response.timeToWait);
    }

    @Override
    public void handle(PollResponse response) {
        clientController.onControllerMessage(response.controllerMessage);
    }

    @Override
    public void handle(OnWinner response) {
        clientController.onWinner(response.winner, response.winnerPoints, response.yourPoints);
    }
}
