package controllerclient;

import controllerresults.ControllerActionResultClient;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

/**
 * This interface contains all the methods used by View classes to communicate with the external world.
 */
public interface View {
    void chooseTarget(GameMapView gameMap, ControllerActionResultClient elem, List<TargetView> target);
    void chooseAction(ControllerActionResultClient elem, List<ActionView> action);
    void chooseWeapon(ControllerActionResultClient elem, List<WeaponView> weapon);

    void rollback();
    void terminated();

    void updateMap(GameMapView gameMapView);
}
