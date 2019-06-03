package view.gui;

import controllerclient.ClientControllerClientInterface;
import controllerclient.View;
import controllerresults.ControllerActionResultClient;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import java.util.List;

public class GuiController implements View {

    private Login login;

    public GuiController(ClientControllerClientInterface controller){
        Login.run(this, controller);
    }

    void attachView(Login login) {
        this.login = login;
    }

    @Override
    public void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor) {
        login.loginResponse(result, invalidUsername, invalidColor);
    }


    @Override
    public void chooseAction(ControllerActionResultClient elem, java.util.List<ActionView> action) {

    }

    @Override
    public void chooseWeapon(ControllerActionResultClient elem, java.util.List<WeaponView> weapon) {

    }

    @Override
    public void chooseTarget(GameMapView gameMap, ControllerActionResultClient elem, List<TargetView> target) {

    }

    @Override
    public void terminated() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void updateMap(GameMapView gameMapView) {

    }



}
