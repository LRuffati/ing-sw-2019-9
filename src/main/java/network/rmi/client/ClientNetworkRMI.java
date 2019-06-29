package network.rmi.client;

import controller.GameMode;
import gamemanager.GameBuilder;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.Database;
import network.Player;
import controller.controllerclient.ClientControllerNetworkInterface;
import network.rmi.server.ServerRMIInterface;
import network.exception.InvalidLoginException;
import controller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *  Contains all the method defined in ServerInterface and ClientInterface.
 */
public class ClientNetworkRMI extends UnicastRemoteObject implements ClientNetworkRMIInterface {

    private transient ServerRMIInterface controller;
    private transient String token;
    private transient ClientControllerNetworkInterface clientController;

    public ClientNetworkRMI(ServerRMIInterface controller, ClientControllerNetworkInterface clientController) throws RemoteException {
        this.controller = controller;
        this.clientController = clientController;
    }

    @Override
    public void setToken(String token) {}

    //ServerInterface methods

    @Override
    public void sendUpdate(String str) {
        System.out.println("Update:\t" + str);
    }

    @Override
    public void ping() {
        clientController.reset();
        try {
            controller.pingResponse(token);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendException(Exception exception) {
        //TODO: restituire il messaggio al controller ??
        //TODO: throw error message
        System.out.println(exception.getMessage());
    }

    @Override
    public void nofifyMap(GameMapView gameMap) {
        clientController.updateMap(gameMap);
    }


    @Override
    public void onConnection(Player player, int numOfPlayer) {
        clientController.onConnection(player, true, numOfPlayer);
    }

    @Override
    public void onDisconnection(Player player, int numOfPlayer) {
        clientController.onConnection(player, false, numOfPlayer);
    }

    @Override
    public void onStarting(String map, GameMode gameMode) {
        clientController.onStarting(map, gameMode);
    }

    @Override
    public void onTimer(int ms) {
        clientController.onTimer(ms);
    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoint) {
        clientController.onWinner(winner, winnerPoints, yourPoint);
    }



    //ClientInterface methods

    @Override
    public boolean register(String username, String password, String color) throws RemoteException, InvalidLoginException {
        String t = controller.register(this, username, password, color);
        this.token = t;
        System.out.println("Il mio token\t" + t);
        return !t.equals("");
    }

    @Override
    public Tuple3<Boolean, Boolean, Tuple<String, GameMode>> reconnect(String username, String password) throws RemoteException, InvalidLoginException {
        Tuple3<String, Boolean, Tuple<String, GameMode>> res = controller.reconnect(this, username, password);
        this.token = res.x;
        return new Tuple3<>(!token.equals(""), res.y, res.z);
    }

    @Override
    public int close() throws RemoteException {
        controller.close(token);
        System.out.println("Permesso di uscita");
        return 0;
    }

    @Override
    public int mirror(int num) throws RemoteException{
        int n = controller.mirror(token, num);
        System.out.println("Mirrored\t" + n);
        return n;
    }

    @Override
    public void modeRequest(boolean normalMode) throws RemoteException {
        controller.modeRequest(normalMode);
    }

    @Override
    public void pick(String choiceId, List<Integer> choices) throws RemoteException {
        ControllerMessage result = controller.pick(token, choiceId, choices);
        clientController.onControllerMessage(result);
    }


    @Override
    public void getMap() throws RemoteException{
        controller.getMap(token);
    }

    @Override
    public void poll() throws RemoteException {
        ControllerMessage message = controller.poll(token);
        clientController.onControllerMessage(message);
    }




    public void run() throws RemoteException, InvalidLoginException {
        run(true, null);
    }
    public void run(boolean newConnection, String token) throws RemoteException, InvalidLoginException {
        if(newConnection)
            register("user", "psw", "blue");
        else
            System.out.println(reconnect("user", "psw"));
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
