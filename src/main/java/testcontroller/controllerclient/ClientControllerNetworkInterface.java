package testcontroller.controllerclient;

import network.Player;
import testcontroller.controllermessage.ControllerMessage;
import viewclasses.GameMapView;

/**
 * Interface implemented by ClientController. It contains all the method that are called directly by the server.
 * Mostly are notify methods.
 */
public interface ClientControllerNetworkInterface {
    void updateMap(GameMapView gameMapView);

    void onControllerMessage(ControllerMessage controllerMessage);

    void onConnection(Player player, boolean connection);
    void onStarting(String map);
    void onTimer(int ms);


    void reset();
}
