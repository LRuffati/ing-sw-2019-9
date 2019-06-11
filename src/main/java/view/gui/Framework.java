package view.gui;

import controllerclient.ClientControllerClientInterface;
import controllerclient.Message;
import controllerclient.View;
import network.Player;
import viewclasses.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Framework implements View {

    private static ClientControllerClientInterface clientController;

    private PhaseType phase;
    private Login login;
    private static boolean loginFlag = false;
    private WaitingScreen waitingScreen;

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
            phase = PhaseType.WAITING;
            loginFlag = true;
            login.dispatchEvent(new WindowEvent(login, WindowEvent.WINDOW_CLOSING));

            waitingScreen = new WaitingScreen(this);
            waitingScreen.addWindowListener(new WindowEventHandler());
            waitingScreen.setVisible(true);
        }
       else {
            login.loginResponse(result, invalidUsername, invalidColor);
        }
    }

    @Override
    public void loginNotif() {
        phase = PhaseType.LOGIN;
        login = new Login(this);
        login.addWindowListener(new WindowEventHandler());
        login.setVisible(true);
    }




    @Override
    public void terminated() {

    }

    @Override
    public void updateMap(GameMapView gameMapView) {

    }

    @Override
    public void chooseTarget(List<TargetView> target, boolean single, boolean optional, String description, GameMapView gameMap, String choiceId){

    }

    @Override
    public void chooseAction(List<ActionView> action, boolean single, boolean optional, String description, String choiceId) {

    }

    @Override
    public void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId) {

    }

    @Override
    public void chooseString(List<String> string, boolean single, boolean optional, String description, String choiceId) {

    }

    @Override
    public void chooseWeapon(List<WeaponView> weapon, boolean single, boolean optional, String description, String choiceId) {

    }

    @Override
    public void onConnection(Player player, boolean connected, int numOfPlayers) {
        switch (phase){
            case LOGIN:
                login.onConnection(player, connected);
                break;
            case WAITING:
                waitingScreen.onConnection(player, connected);
                break;
            case GAME:
                //todo
                break;
            case TERMINATED:
                break;

                default:
                    break;
        }
    }

    @Override
    public void onTimer(int timeToCount) {

    }

    @Override
    public void onStarting(String map) {

    }

    @Override
    public void onMessage(Message message) {

    }

    @Override
    public void onRespawn() {

    }

    @Override
    public void onRollback() {

    }

    @Override
    public void onTakeback() {

    }

    @Override
    public void onTerminator() {

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
