package network.socket.client;

import network.socket.messages.notify.*;
import testcontroller.controllerclient.ClientControllerNetworkInterface;
import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.ClientInterface;
import network.exception.InvalidLoginException;
import network.socket.messages.*;
import view.View;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.io.IOException;
import java.util.List;

/**
 * This class handles all the methods called by the client (implemented in ClientInterface)
 * and the Requests called by the server (implemented in ResponseHandler)
 */
public class ClientNetworkSocket implements ResponseHandler, ClientInterface {
    private final Client client;
    private Thread receiver;

    private View view;
    private ClientControllerNetworkInterface clientController;


    public ClientNetworkSocket(Client client, ClientControllerNetworkInterface clientController, View view){
        this.client = client;
        this.clientController = clientController;
        this.view = view;
        run();
    }

    private void sync(){
        synchronized (client) {
            try {
                //TODO: add while loop
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
    public boolean reconnect(String username, String password) throws InvalidLoginException {
        client.request(new ReconnectRequest(username, password));
        sync();
        ClientContext c = ClientContext.get();
        if(c.getConnectionResult()) {
            String token = ClientContext.get().getToken();
            System.out.println("Reconnected with: token\n" + token);
            return true;
        }
        else {
            throw new InvalidLoginException("Invalid reconnection", c.getLoginException().x, c.getLoginException().y);
        }
    }

    /**
     * @param type 0 for pickTarg, 1 for pickWeapon, 2 for pickAction
     * @param chooserId Id of the chosen container
     * @param choice A list containing all the chosen index. Server will analyze if a List or an int is needed
     */
    private ControllerActionResultClient pick(int type, String chooserId, List<Integer> choice){
        client.request(new PickRequest(type, chooserId, choice));
        sync();
        return ClientContext.get().getPickElement();
    }

    @Override
    public ControllerActionResultClient pickTarg(String choiceMakerId, int choice) {
        return pick(0, choiceMakerId, List.of(choice));
    }

    @Override
    public ControllerActionResultClient pickWeapon(String weaponChooserId, List<Integer> choice) {
        return pick(1, weaponChooserId, choice);
    }

    @Override
    public ControllerActionResultClient pickAction(String actionChooserId, int choice) {
        return pick(2, actionChooserId, List.of(choice));
    }

    private void showOptions(int type, String chooserId){
        client.request(new ShowOptionsRequest(ClientContext.get().getToken(), type, chooserId));
        sync();
    }

    @Override
    public Tuple<Boolean, List<TargetView>> showOptionsTarget(String choiceMakerId) {
        showOptions(0, choiceMakerId);
        return ClientContext.get().getShowOptionsTarget();
    }

    @Override
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) {
        showOptions(1, weaponChooserId);
        return ClientContext.get().getShowOptionsWeapon();
    }

    @Override
        public Tuple<Boolean, List<ActionView>> showOptionsAction(String actionPickerId) {
        showOptions(2, actionPickerId);
        return ClientContext.get().getShowOptionsAction();
    }

    @Override
    public GameMapView getMap(String gameMapId) {
        client.request(new GetMapRequest(gameMapId));
        sync();
        return ClientContext.get().getGameMapView();
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
        ClientContext.get().setPickElement(response.result);
        desync();
    }

    @Override
    public void handle(ShowOptionsResponse response) {
        ClientContext.get().setShowOptions(response.result);
        desync();
    }

    @Override
    public void handle(ExceptionResponse response) {
        //TODO: restituire il messaggio al controller
        //TODO: throw error message
        System.out.println(response.exception.getMessage());
    }

    @Override
    public void handle(GetMapResponse response) {
        ClientContext.get().setGameMapView(response.gameMapView);
        desync();
    }



    //ClientControllerNetworkInterface methods
    @Override
    public void handle(NotifyMap response) {
        clientController.updateMap(response.gameMap);
    }

    @Override
    public void handle(OnConnection response) {
        view.onConnection(response.playerConnected, response.connected);
    }

    @Override
    public void handle(OnStarting response) {
        view.onStarting(response.mapName);
    }

    @Override
    public void handle(OnTimer response) {
        view.onTimer(response.timeToWait);
    }
}
