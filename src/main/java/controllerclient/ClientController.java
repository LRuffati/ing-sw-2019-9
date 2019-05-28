package controllerclient;


import controllerresults.ControllerActionResultClient;
import genericitems.Tuple;
import network.ClientInterface;
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

    public void login(String username, String password, String color) {

    }

    public void login(String username, String password) {

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


    /**
     * This method is called by the View to notify the Server that a new choice has been made.
     * @param elem Contains the type of the action to be performed and an identifier of the action.
     * @param choices A list containing all the index of elements that have been chosen. PickTarget and PickAction only analyze the first element of the List.
     */
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

    /**
     * This method is used by the client to restart the action.
     */
    @Override
    public void restartSelection() throws RemoteException {
        ControllerActionResultClient first = stack.getFirst();
        stack.clear();
        gameMapViewMap.clear();
        newSelection(first);
    }

    /**
     * This method is used by the client to repeat the last selection
     */
    @Override
    public void rollback() throws RemoteException {
        stack.pop();
        newSelection(stack.pop());
    }

}
