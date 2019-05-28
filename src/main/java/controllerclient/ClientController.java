package controllerclient;


import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.ClientInterface;
import network.exception.InvalidLoginException;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;

import java.rmi.RemoteException;
import java.util.*;

/**
 * This class is used to store the data needed to the client and to send him notification.
 * This is the only access used by the View to receive messages and to query the Server.
 */
public class ClientController implements ClientControllerClientInterface{
    private View view;
    private ClientInterface network;

    private Deque<ControllerActionResultClient> stack;
    private Map<String, GameMapView> gameMapViewMap;

    /**
     * Builder of the Class
     * @param view The view that must be associated with this ClientController. All the messages will be sent to this view.
     * @param network The network that must be associated with this ClientController.
     */
    public ClientController(View view, ClientInterface network) {
        this.view = view;
        this.network = network;

        stack = new ArrayDeque<>();
        gameMapViewMap = new HashMap<>();
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

}
