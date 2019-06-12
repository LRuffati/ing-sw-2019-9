package controllerclient;

import viewclasses.GameMapView;

/**
 * Interface implemented by ClientController. It contains all the method that are called directly by the server.
 * Mostly are notify methods.
 */
public interface ClientControllerNetworkInterface {
    void reset();
    void updateMap(GameMapView gameMapView);
}
