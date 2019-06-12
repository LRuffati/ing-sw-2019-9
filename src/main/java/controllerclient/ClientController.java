package controllerclient;


import cli.CLIDemo;
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
import view.gui.Framework;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to store the data needed to the client and to send him notification.
 * This is the only access used by the View to receive messages and to query the Server.
 */
public class ClientController implements ClientControllerClientInterface, ClientControllerNetworkInterface {
    private Logger logger;

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
        logger = Logger.getLogger(ClientController.class.getName());

        if (cli)
            view = new CLIDemo(this);
        else
            Framework.run(this);

        if(socket) {
            Client client = new Client(networkAddress, ParserConfiguration.parseInt("SocketPort"));
            client.init();
            network = new ClientNetworkSocket(client, this);
        }
        else {
            Registry registry = LocateRegistry.getRegistry(networkAddress, ParserConfiguration.parseInt("RMIPort"));
            ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(ParserConfiguration.parse("RMIBinding"));
            //ServerRMIInterface controller = (ServerRMIInterface)Naming.lookup(ParserConfiguration.parse("RMIBinding"));

            //String lookup = String.format("//%s:%d/%s", networkAddress, ParserConfiguration.parseInt("RMIPort"), "prova");//ParserConfiguration.parse("RMIBinding"));
            //Registry registry = LocateRegistry.getRegistry();
            //ServerRMIInterface controller = (ServerRMIInterface) registry.lookup(lookup);
            //ServerRMIInterface controller = (ServerRMIInterface) Naming.lookup(lookup);
            network = new ClientNetworkRMI(controller, this);
        }

        view.loginNotif();

        stack = new ArrayDeque<>();
        gameMapViewMap = new HashMap<>();

        //Main.register(network);
        //Main.run(network);
    }

    /**
     * Method used by the View to notify its presence. Once the view is connected all the messages are sent to her.
     * @param view The view that want to receive the messages.
     */
    public void attachView(View view) {
        this.view = view;
    }


    @Override
    public void login(String username, String password, String color) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    network.register(username, password, color.toLowerCase());
                    view.loginResponse(true, false, false);
                    add();
                } catch (
                        InvalidLoginException e) {
                    view.loginResponse(false, e.wrongUsername, e.wrongColor);
                } catch (
                        RemoteException e) {
                    logger.log(Level.SEVERE, "Exception in login (register)", e);
                }
                Thread.currentThread().interrupt();
            }

        };
        thread.start();
    }

    @Override
    public void login(String username, String password) {
        try {
            network.reconnect(username, password);
            view.loginResponse(true, false, false);
            add();
        }
        catch (InvalidLoginException e) {
            view.loginResponse(false, e.wrongUsername, e.wrongColor);
        }
        catch (RemoteException e) {
            logger.log(Level.SEVERE, "Exception in login (reconnect)", e);
        }
    }

    @Override
    public void quit() {
        try {
            network.close();
        }
        catch (RemoteException e) {
            logger.log(Level.SEVERE, "Exception in quit", e);
        }
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



    @Override
    public void pick(String id, List<Integer> choices) {

    }

    @Override
    public void restartSelection() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public GameMapView getMap() {
        return null;
    }


    //ClientControllerNetworkInterface methods

    @Override
    public void updateMap(GameMapView gameMap) {
        this.gameMap = gameMap;
        view.updateMap(gameMap);
    }




    private Timer timer = new Timer();

    public void add() {
        timer = new Timer("TimerForDisconnection");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                stop();
                quit();
            }
        };

        timer.schedule(timerTask, 200);
    }

    public void reset() {
        try {
            timer.cancel();
            add();
        }
        catch (NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        timer.cancel();
    }
}
