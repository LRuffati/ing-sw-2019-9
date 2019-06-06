package view.gui;

import testcontroller.controllerclient.ClientControllerClientInterface;
import view.View;
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

    private Framework(ClientControllerClientInterface controller){
        //controller.attachView(this);

        login = new Login(this);
        JFrame frame = login;
        frame.addWindowListener(new WindowEventHandler());
        frame.setVisible(true);
    }

    private static Framework createAndShowGUI(ClientControllerClientInterface controller) {
        clientController = controller;
        return new Framework(controller);
    }

    public static Framework run(ClientControllerClientInterface controller) {
        return createAndShowGUI(controller);
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
            System.out.println("exit");
            clientController.quit();
        }
    }

}
