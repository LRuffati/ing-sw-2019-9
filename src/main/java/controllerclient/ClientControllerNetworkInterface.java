package controllerclient;

import viewclasses.GameMapView;

import java.rmi.RemoteException;

public interface ClientControllerNetworkInterface {
    void updateMap(GameMapView gameMapView);
}
