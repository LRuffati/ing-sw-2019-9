package network.rmi.server;

import controller.GameMode;
import gamemanager.GameBuilder;
import genericitems.Tuple;
import genericitems.Tuple3;
import network.Database;
import network.ObjectMap;
import network.ServerInterface;
import network.TimerForDisconnection;
import network.exception.InvalidLoginException;
import controller.controllermessage.ControllerMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

/**
 * Contains all the method defined in {@link ServerRMIInterface ServerRMIInterface}.
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

    @Override
    public void pingResponse(String token) {
        TimerForDisconnection.reset(token);
    }


    @Override
    public String register(ServerInterface serverInterface, String username, String password, String color) throws RemoteException, InvalidLoginException {
        return Database.get().login(serverInterface, true, username, password, color);
    }

    @Override
    public Tuple3<String, Boolean, Tuple<String, GameMode>> reconnect(ServerInterface client, String username, String password) throws RemoteException, InvalidLoginException{
        if(username == null || password == null)
            return new Tuple3<>("", false, null);
        String token = Database.get().login(client, true, username,password);
        Tuple<String, GameMode> ret = new Tuple<>("", GameMode.NORMAL);
        if(Database.get().getMainController().isGameStarted())
            ret = new Tuple<>(GameBuilder.get().getMapName(), GameBuilder.get().getGameMode());
        return new Tuple3<>(
                token,
                Database.get().getMainController().isGameStarted(),
                ret
        );
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
    public void modeRequest(boolean normalMode) {
        Database.get().getMainController().modeRequest(normalMode);
    }

    @Override
    public ControllerMessage pick(String token, String choiceId, List<Integer> choices) throws RemoteException {
        checkConnection(token);
        return ObjectMap.get().pick(token, choiceId, choices);
    }


    @Override
    public void getMap(String token) {
        try {
            checkConnection(token);
            Database d = Database.get();
            d.getNetworkByToken(token).nofifyMap(d.getControllerByToken(token).sendMap());
        }
        catch (RemoteException e) {
            e.printStackTrace();
            Database.get().logout(token);
        }
    }

    @Override
    public ControllerMessage poll(String token) throws RemoteException {
        checkConnection(token);
        ControllerMessage msg = Database.get().getControllerByToken(token).getInstruction();

        msg = ObjectMap.get().poll(token, msg);
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
