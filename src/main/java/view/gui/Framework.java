package view.gui;

import testcontroller.Message;
import view.View;
import network.Player;
import testcontroller.controllerclient.ClientControllerClientInterface;
import viewclasses.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Framework implements View {

    private static ClientControllerClientInterface clientController;

    private PhaseType phase;
    private Login login;
    private GUIMap1 game;
    private static boolean changeFrameFlag = false;
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
            changeFrameFlag = true;
            login.dispatchEvent(new WindowEvent(login, WindowEvent.WINDOW_CLOSING));

            waitingScreen = new WaitingScreen(this);
            waitingScreen.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        login.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        waitingScreen.onTimer(timeToCount);
    }

    @Override
    public void onStarting(String map) {
        phase = PhaseType.GAME;
        changeFrameFlag = true;
        waitingScreen.dispatchEvent(new WindowEvent(waitingScreen, WindowEvent.WINDOW_CLOSING));

        //game = new GUIMap1();
        //game.addWindowListener(new WindowEventHandler());
        game.setVisible(true);
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
            if(!changeFrameFlag) {
                System.out.println("exit");
                clientController.quit();
            } else {
                changeFrameFlag = false;
            }
        }
    }

}
