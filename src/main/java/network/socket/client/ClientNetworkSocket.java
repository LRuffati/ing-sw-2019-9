package network.socket.client;

import actions.ActionTemplate;
import actions.targeters.targets.Targetable;
import controllerresults.ActionResultType;
import genericitems.Tuple;
import network.ClientInterface;
import network.Database;
import network.socket.messages.*;
import viewclasses.WeaponView;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

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
    public void register(){
        client.request(new RegisterRequest("a", "blue"));
        sync();
        System.out.println("token\n" + ClientContext.get().getToken());
    }

    @Override
    public boolean reconnect(String token) throws RemoteException {
        client.request(new ReconnectRequest(token));
        sync();
        return ClientContext.get().getReconnected();
    }

    /**
     * @param type 0 for pickTarg, 1 for pickWeapon, 2 for pickAction
     * @param chooserId Id of the chosen container
     * @param choice A list containing all the chosen index. Server will analyze if a List or an int is needed
     */
    private Tuple<ActionResultType, String> pick(int type, String chooserId, List<Integer> choice){
        client.request(new PickRequest(ClientContext.get().getToken(), type, chooserId, choice));
        sync();
        return ClientContext.get().getPickElement();
    }

    @Override
    public Tuple<ActionResultType, String> pickTarg(String choiceMakerId, int choice) {
        return pick(0, choiceMakerId, List.of(choice));
    }

    @Override
    public Tuple<ActionResultType, String> pickWeapon(String weaponChooserId, List<Integer> choice) {
        return pick(1, weaponChooserId, choice);
    }

    @Override
    public Tuple<ActionResultType, String> pickAction(String actionChooserId, int choice) {
        return pick(2, actionChooserId, List.of(choice));
    }

    private void showOptions(int type, String chooserId){
        client.request(new ShowOptionsRequest(ClientContext.get().getToken(), type, chooserId));
        sync();
    }

    @Override
    public Tuple<Boolean, List<Targetable>> showOptionsTarget(String choiceMakerId) {
        showOptions(0, choiceMakerId);
        return ClientContext.get().getShowOptionsTarget();
    }

    @Override
    public List<WeaponView> showOptionsWeapon(String weaponChooserId) {
        showOptions(1, weaponChooserId);
        return ClientContext.get().getShowOptionsWeapon();
    }

    @Override
        public Tuple<Boolean, List<ActionTemplate>> showOptionsAction(String actionPickerId) {
        showOptions(2, actionPickerId);
        return ClientContext.get().getShowOptionsAction();
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
    public void handle(ReconnectResponse response) {
        ClientContext.get().setReconnected(response.result);
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
    }

    @Override
    public void handle(ExceptionResponse response) {
        //TODO: restituire il messaggio al controller
        //TODO: throw error message
        System.out.println(response.exception.getMessage());
    }
}
