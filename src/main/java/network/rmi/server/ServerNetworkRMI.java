package network.rmi.server;

import network.Database;
import network.ObjectMap;
import network.ServerInterface;
import network.TimerForDisconnection;
import network.exception.InvalidLoginException;
import testcontroller.controllerclient.ControllerMessageClient;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import viewclasses.GameMapView;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Contains all the method defined in ServerRMIInterfaces.
 * This class only handle messages received from the Client.
 */
public class ServerNetworkRMI extends UnicastRemoteObject implements ServerRMIInterface{

    transient Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerNetworkRMI() throws RemoteException{
        super();
    }

    private boolean checkConnection(String token) throws RemoteException {
        if (!Database.get().getConnectedTokens().contains(token))
            throw new RemoteException("Invalid Token");

        return Database.get().getConnectedTokens().contains(token);
    }

    public void sendPing() throws RemoteException {
        Database d = Database.get();
        for(ServerInterface client :  d.getConnectedTokens().stream().map(d::getNetworkByToken).collect(Collectors.toList())){
            try {
                client.ping();
            }
            catch (ConnectException e) {
                d.logout(client);
            }
        }
    }

    @Override
    public void pingResponse(String token) {
        TimerForDisconnection.reset(token);
    }


    @Override
    public String register(ServerInterface serverInterface, String username, String password, String color) throws RemoteException, InvalidLoginException {
        return Database.get().login(serverInterface, username, password, color);
    }

    @Override
    public String reconnect(ServerInterface client, String username, String password) throws RemoteException, InvalidLoginException{
        if(username == null || password == null)
            return "";
        return Database.get().login(client, username, password);
    }

    @Override
    public int mirror(String token, int num) throws RemoteException {
        checkConnection(token);
        System.out.println("Request di mirror\t" + num);
        return num;
    }

    @Override
    public int close(String token) {
        Database.get().logout(token);
        System.out.println("Richiesta di uscita");
        return 0;
    }



    @Override
    public ControllerMessage pick(String token, String choiceId, List<Integer> choices) throws RemoteException {
        checkConnection(token);
        return ObjectMap.get().pick(token, choiceId, choices);
    }


    @Override
    public GameMapView getMap(String token) throws RemoteException {
        checkConnection(token);
        return Database.get().getControllerByToken(token).sendMap();
    }

    @Override
    public ControllerMessage poll(String token) throws RemoteException {
        checkConnection(token);
        ControllerMessage msg = Database.get().getControllerByToken(token).getInstruction();
        if(!msg.type().equals(SlaveControllerState.WAIT))
            msg = ObjectMap.get().init(msg);
        else
            msg = new ControllerMessageClient(msg, null);
        return msg;
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
