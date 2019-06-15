package testcontroller;

import cli.CLIDemo;
import gamemanager.ParserConfiguration;
import network.ClientInterface;
import network.Player;
import network.exception.InvalidLoginException;
import network.rmi.client.ClientNetworkRMI;
import network.rmi.server.ServerRMIInterface;
import network.socket.client.Client;
import network.socket.client.ClientNetworkSocket;
import testcontroller.controllerclient.ClientControllerClientInterface;
import testcontroller.controllerclient.ClientControllerNetworkInterface;
import testcontroller.controllerclient.ControllerMessageClient;
import testcontroller.controllermessage.ControllerMessage;
import testcontroller.controllerstates.SlaveControllerState;
import view.View;
import view.gui.Framework;
import viewclasses.GameMapView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the class which interacts directly with the user, in the distributed system
 * It'll be separated from the other parts of the controlle by the network layer
 *
 *
 * This class is used to store the data needed to the client and to send him notification.
 * This is the only access used by the View to receive messages and to query the Server.
 */
public class ClientController implements ClientControllerClientInterface, ClientControllerNetworkInterface {
    private Logger logger;

    private View view;
    private ClientInterface network;


    private Deque<ControllerMessage> stack;
    private Map<String, GameMapView> gameMapViewMap;

    private String currentGameMap;
    private GameMapView gameMap;

    private TimerTask repeatedTask;
    private Timer timerForPolling;
    private boolean polling;

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

        initPolling();

        //Main.register(network);
        //Main.run(network);
    }

    /**
     * Method used by the View to notify its presence. Once the view is connected all the messages are sent to her.
     * @param view The view that want to receive the messages.
     */
    @Override
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
            System.out.println("QUIT");
            network.close();
        }
        catch (RemoteException e) {
            logger.log(Level.SEVERE, "Exception in quit", e);
        }
    }


    @Override
    public void pick(String id, List<Integer> choices) {
        network.pick(id, choices);
    }

    @Override
    public void restartSelection() {
        ControllerMessage first = stack.getFirst();
        stack.clear();
        gameMapViewMap.clear();
        elaborate(first);
    }

    @Override
    public void rollback() {
        stack.pop();
        elaborate(stack.pop());
    }

    @Override
    public void onControllerMessage(ControllerMessage controllerMessage) {
        elaborate(controllerMessage);
    }

    private void elaborate(ControllerMessage controllerMessage) {
        if(controllerMessage.getMessage() != null) {
            view.onMessage(controllerMessage.getMessage());
        }

        if(!currentGameMap.equals(controllerMessage.gamemap())) {
            currentGameMap = controllerMessage.gamemap();
            network.getMap();
        }

        if(controllerMessage.type().equals(SlaveControllerState.WAIT)) {
            stack.clear();
            if (!polling) {
                setPolling(true);
                view.terminated();
            }
        }
        else {
            stack.push(controllerMessage);
            if(polling)    setPolling(false);
        }


        switch (controllerMessage.type()) {
            case MAIN:
                break;
            case RESPAWN:
                view.onRespawn();
                break;
            case USETAGBACK:
                view.onTakeback();
                break;
            case TERMINATOR:
                view.onTerminator();
                break;
            case ROLLBACK:
                view.onRollback();
                rollback();
                break;

                default:
                    break;
        }

        ChoiceBoard choice = controllerMessage.genView();
        String id = ((ControllerMessageClient)controllerMessage).id;
        if(choice!= null) {
            switch (choice.type) {
                case STRING:
                    view.chooseString(choice.stringViews, choice.single, choice.optional, choice.description, id);
                    break;
                case ACTION:
                    view.chooseAction(choice.actionViews, choice.single, choice.optional, choice.description, id);
                    break;
                case TARGET:
                    view.chooseTarget(choice.targetViews, choice.single, choice.optional,
                            choice.description, controllerMessage.sandboxView(), id);
                    break;
                case WEAPON:
                    view.chooseWeapon(choice.weaponViews, choice.single, choice.optional, choice.description, id);
                    break;
                case POWERUP:
                    view.choosePowerUp(choice.powerUpViews, choice.single, choice.optional, choice.description, id);
                    break;

                    default:
                        break;
            }
        }

    }


    private void initPolling() {
        repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("Polling");
                network.poll();
            }
        };
        timerForPolling = new Timer("TimerForPolling");
    }

    private void setPolling(boolean value) {
        if(polling != value)
            if(value) {
                timerForPolling.scheduleAtFixedRate(repeatedTask, 0, 2000);
            }
            else {
                timerForPolling.cancel();
            }
        polling = value;
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

    @Override
    public void onStarting(String map) {
        setPolling(true);
        view.onStarting(map);
    }

    @Override
    public void onTimer(int ms) {
        view.onTimer(ms);
    }

    @Override
    public void onConnection(Player player, boolean connection) {
        view.onConnection(player, connection);
    }




    private Timer timer = new Timer();
    private int signals = 0;
    private boolean stopped = false;

    public void add() {
        timer = new Timer("TimerForDisconnection");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                signals++;
                if(signals >= 3)
                    System.out.println("error\t" + signals);
                if(signals >= 10) {
                    System.out.println("BEFOREQUIT");
                    stop();
                    quit();
                    Thread.currentThread().interrupt();
                }
            }
        };

        timer.schedule(timerTask, 200,500);
    }

    public void reset() {
        if(!stopped) {
            signals = 0;
        }
    }

    public void stop() {
        stopped = true;
        timer.cancel();
    }
}
