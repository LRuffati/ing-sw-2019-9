package controllerclient;


import controllerresults.ControllerActionResultClient;
import gamemanager.ParserConfiguration;
import genericitems.Tuple;
import network.ClientInterface;
import network.Main;
import network.exception.InvalidLoginException;
import network.rmi.client.ClientNetworkRMI;
import network.rmi.server.ServerRMIInterface;
import network.socket.client.Client;
import network.socket.client.ClientNetworkSocket;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * This class is used to store the data needed to the client and to send him notification.
 * This is the only access used by the View to receive messages and to query the Server.
 */
public class ClientController implements ClientControllerClientInterface, ClientControllerNetworkInterface {
    private View view;
    private ClientInterface network;

    private GameMapView gameMap;

    private Deque<ControllerActionResultClient> stack;
    private Map<String, GameMapView> gameMapViewMap;


    /**
     * Builder of the class. This generates the View (cli or GUI) and the Network (Socket or RMI), depending by the choices of the user.
     * @param socket true if a socket connection is required. False if a RMI connection is required
     * @param cli true if Cli is required. False is Gui is required
     * @param networkAddress Contains the address used by the Network to connect with the Server
     */
    public ClientController(boolean socket, boolean cli, String networkAddress) throws NotBoundException, IOException {
        //view = cli ? new CLIDemo() : new GUI();
        //view = cli ? new CLIDemo(this) : null;

        if(socket) {
            Client client = new Client(networkAddress, ParserConfiguration.parseInt("SocketPort"));
            client.init();
            network = new ClientNetworkSocket(client, this);
        }
        else {
            Registry registry = LocateRegistry.getRegistry();
            String lookup = String.format("//%s:%d/controller", networkAddress, ParserConfiguration.parseInt("RMIPort"));
            ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(lookup);
            network = new ClientNetworkRMI(controller);
        }

        stack = new ArrayDeque<>();
        gameMapViewMap = new HashMap<>();

        Main.register(network);
        Main.run(network);
    }

    /**
     * Method used by the View to notify its presence. Once the view is connected all the messages are sent to her.
     * @param view The view that want to receive the messages.
     */
    public void attachView(View view) {
        this.view = view;
    }


    @Override
    public boolean login(String username, String password, String color) throws RemoteException, InvalidLoginException {
        return network.register(username, password, color);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException, InvalidLoginException {
        return network.reconnect(username, password);
    }

    @Override
    public void quit() throws RemoteException {
        network.close();
    }


    private GameMapView getMap(TargetView targetView) throws RemoteException {
        String mapUid = targetView.getGameMapViewId();
        if(!gameMapViewMap.containsKey(mapUid)) {
            GameMapView mapView = network.getMap(mapUid);
            gameMapViewMap.put(mapUid, mapView);

            return mapView;
        }
        return gameMapViewMap.get(mapUid);
    }

    /**
     * This method notify the View that a new choice can be done.
     * Depending on the type of selection different actions can be performed.
     * If the action is terminated the stack of the passed action and the Map containing all the GameMapViews are deleted.
     * @param elem A ControllerActionResultClient that specify the type of the Request and some other details.
     */
    public void newSelection(ControllerActionResultClient elem) throws RemoteException {
        switch (elem.type){
            case PICKTARGET:
                stack.push(elem);
                Tuple<Boolean, List<TargetView>> resTarget = network.showOptionsTarget(elem.actionId);
                GameMapView gameMapView = getMap(resTarget.y.iterator().next());
                view.chooseTarget(gameMapView, elem, resTarget.y);
                break;

            case PICKACTION:
                stack.push(elem);
                Tuple<Boolean, List<ActionView>> resAction = network.showOptionsAction(elem.actionId);
                view.chooseAction(elem, resAction.y);
                break;

            case PICKWEAPON:
                stack.push(elem);
                view.chooseWeapon(elem, network.showOptionsWeapon(elem.actionId));
                break;

            case ROLLBACK:
                newSelection(stack.pop());
                view.rollback();
                break;
            case TERMINATED:
                stack.clear();
                gameMapViewMap.clear();
                view.terminated();
                break;

            default:
                break;
        }
    }


    @Override
    public void pick(ControllerActionResultClient elem, List<Integer> choices) throws RemoteException {
        switch (elem.type) {
            case PICKTARGET:
                newSelection(network.pickTarg(elem.actionId, choices.get(0)));
                break;
            case PICKACTION:
                newSelection(network.pickAction(elem.actionId, choices.get(0)));
                break;
            case PICKWEAPON:
                newSelection(network.pickWeapon(elem.actionId, choices));
                break;

            default:
                break;
        }
    }

    @Override
    public void restartSelection() throws RemoteException {
        ControllerActionResultClient first = stack.getFirst();
        stack.clear();
        gameMapViewMap.clear();
        newSelection(first);
    }

    @Override
    public void rollback() throws RemoteException {
        stack.pop();
        newSelection(stack.pop());
    }

    @Override
    public GameMapView getMap() {
        return gameMap;
    }


    //ClientControllerNetworkInterface methods

    @Override
    public void updateMap(GameMapView gameMap) {
        this.gameMap = gameMap;
        view.updateMap(gameMap);
    }
}
