package view.gui;

import controllerclient.ClientControllerClientInterface;
import controllerclient.View;
import controllerresults.ControllerActionResultClient;
import viewclasses.ActionView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.WeaponView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Framework implements View {

    private static ClientControllerClientInterface clientController;

    private Login login;
    private static boolean loginFlag = false;

    private Framework(ClientControllerClientInterface controller){
        controller.attachView(this);


    }

    private static void createAndShowGUI(ClientControllerClientInterface controller) {
        clientController = controller;
        new Framework(controller);
    }

    public static void run(ClientControllerClientInterface controller) {
        createAndShowGUI(controller);
    }

    public ClientControllerClientInterface getClientController() {
        return clientController;
    }

    @Override
    public void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor) {
        if(result) {
            loginFlag = true;
            login.dispatchEvent(new WindowEvent(login, WindowEvent.WINDOW_CLOSING));
        }
       // login.loginResponse(result, invalidUsername, invalidColor);
    }

    @Override
    public void loginNotif() {
        login = new Login(this);
        JFrame frame = login;
        login.addWindowListener(new WindowEventHandler());
        login.setVisible(true);
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


    public void quit(JFrame frame) {
        if (quitConfirmed(frame)) {
            System.out.println("Quitting.");
            clientController.quit();
            System.exit(0);
        }
        System.out.println("Quit operation not confirmed; staying alive.");
    }

    private boolean quitConfirmed(JFrame frame) {
        String s1 = "Quit";
        String s2 = "Cancel";
        Object[] options = {s1, s2};
        int n = JOptionPane.showOptionDialog(frame,
                "Do you really want to quit?\nYou won't be able to log in again.",
                "Quit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                s1);
        return n == JOptionPane.YES_OPTION;
    }

    static class WindowEventHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent evt) {
            if(!loginFlag) {
                System.out.println("exit");
                clientController.quit();
            } else {
                loginFlag = false;
            }
        }
    }

}
