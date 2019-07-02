package view.gui;

import controller.GameMode;
import controller.Message;
import view.View;
import network.Player;
import controller.controllerclient.ClientControllerClientInterface;
import viewclasses.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Framework implements View {

    private static ClientControllerClientInterface clientController;

    private PhaseType phase;
    private Login login;
    private GameFrame game;
    private static boolean changeFrameFlag = false;
    private WaitingScreen waitingScreen;
    private GameMapView gmv;
    private String toPick;

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

        waitingScreen = new WaitingScreen(this);
        waitingScreen.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        waitingScreen.addWindowListener(new WindowEventHandler());
        waitingScreen.setVisible(false);
    }


    @Override
    public void terminated() {
        game.getOutputBox().writeOnOutput("Action finally executed.");
    }

    @Override
    public void updateMap(GameMapView gameMapView, boolean forced) {
        this.gmv = gameMapView;
        if(game == null) game = new GameFrame(gmv, this, 1);
        game.getMap().drawPlayers(gameMapView);
    }

    public void pick(List<Integer> picked) {
        clientController.pick(toPick, picked);
    }

    @Override
    public void chooseTarget(List<TargetView> target, boolean single, boolean optional, String description, GameMapView gameMap, String choiceId){
        game.getOutputBox().writeOnOutput(description);

        toPick = choiceId;
        game.getMap().clickableButton(target,description, single, optional);
    }

    @Override
    public void chooseAction(List<ActionView> actions, boolean single, boolean optional, String description, String choiceId) {
        game.getOutputBox().writeOnOutput(description);

        String[] names = new String[100];
        int i = 0;
        for(ActionView av : actions){
            game.getOutputBox().writeOnOutput(i + ". " + av.getName());
            names[i] = av.getName();
            i++;
        }
        names[i] = "Stop";
        i++;
        names[i] = "Rollback";
        i++;
        names[i] = "Reset";
        List<Integer> res = new ArrayList<>();
        Integer choice;
        boolean flag = true;
        while(flag){
            String input  = JOptionPane.showInputDialog(description);

            while(!input.matches(" ?\\d+")){
                JOptionPane.showMessageDialog(null, "You must choose one number from the ones showed.", "ERROR", JOptionPane.ERROR_MESSAGE);
                input  = JOptionPane.showInputDialog("Scegli un'azione.");
            }
            choice = Integer.parseInt(input);

            if(choice.equals(0)){
                if(res.isEmpty()&&optional){
                    flag = false;
                } else if(res.isEmpty()){
                    JOptionPane.showMessageDialog(null,"You must choose at least one Action!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else if(choice.equals(200)&&!res.isEmpty()){
              res.remove(res.size()-1);
            } else if(choice.equals(100)){
                res.clear();
            } else {
                if(!res.contains(choice)) {
                    res.add(choice-1);
                    if(single) flag = false;
                } else {
                    JOptionPane.showMessageDialog(null,"You must choose a different Action", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        clientController.pick(choiceId, res);
    }

    @Override
    public void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId) {
        game.getOutputBox().writeOnOutput(description);
        int i = 0;
        List<Integer> res = new ArrayList<>();
        String[] names = new String[100];
        JFrame puPopUp = new JFrame();
        puPopUp.setUndecorated(true);
        puPopUp.setLayout(new GridLayout(1,0));
        for(PowerUpView pu : powerUp){

            BufferedImage puCard = new PUCard(pu).getCard();
            ImageIcon puCardImage = new ImageIcon(puCard);
            JLabel cardLabel = new JLabel(puCardImage);
            //JButton puBtn = new JButton(puCardImage);
            int finalI = i;
            puPopUp.getContentPane().add(cardLabel);

            puPopUp.setSize(puCardImage.getIconWidth(), puCardImage.getIconHeight());

            puPopUp.setLocation(i * 170, 0);
            puPopUp.setVisible(true);
            names[i] = pu.type().toString();

            /*
            puBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    res.add(finalI);
                    puPopUp.dispose();
                    //flag[0] = false;
                    clientController.pick(choiceId, res);
                }
            });

             */

            i++;
            game.getOutputBox().writeOnOutput(i + ". " + pu.type().toString() + "\n");
        }

        puPopUp.setSize(170*i,264);
        /*while (flag[0]){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        Integer choice;
        boolean flag = true;
        while(flag){

            String input  = JOptionPane.showInputDialog(description);

            while(!input.matches(" ?\\d+")){
                JOptionPane.showMessageDialog(null, "You must choose one number from the ones showed.", "ERROR", JOptionPane.ERROR_MESSAGE);
                input  = JOptionPane.showInputDialog("Riprova e scegli un power up.");
            }
            choice = Integer.parseInt(input);

            if(choice.equals(0)){
                if(res.isEmpty()&&optional){
                    flag = false;
                } else if(res.isEmpty()){
                    JOptionPane.showMessageDialog(null,"You must choose at least one!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else if(choice.equals(200)&&!res.isEmpty()){
                res.remove(res.size()-1);
            } else if(choice.equals(100)){
                res.clear();
            } else {
                if(!res.contains(choice)) {
                    res.add(choice-1);
                    if(single) flag = false;
                } else {
                    JOptionPane.showMessageDialog(null,"You must choose a different Powerup!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        puPopUp.dispose();



        clientController.pick(choiceId, res.stream().map(x->x-1).collect(Collectors.toList()));
        game.getOutputBox().writeOnOutput("clear");
    }

    @Override
    public void chooseString(List<String> string, boolean single, boolean optional, String description, String choiceId) {
        game.getOutputBox().writeOnOutput(description);

        String[] names = new String[100];
        int i = 0;
        for(String str : string){
            names[i] = str;
            i++;
            game.getOutputBox().writeOnOutput(i + ". " + str + "\n");
        }
        names[i] = "Stop";
        i++;
        names[i] = "Rollback";
        i++;
        names[i] = "Reset";
        List<Integer> res = new ArrayList<>();
        Integer choice;
        boolean flag = true;
        while(flag){

            String input  = JOptionPane.showInputDialog(description);

            while(!input.matches(" ?\\d+")){
                JOptionPane.showMessageDialog(null, "You must choose one number from the ones showed.", "ERROR", JOptionPane.ERROR_MESSAGE);
                input  = JOptionPane.showInputDialog("Riprova e scegli.");
            }
            choice = Integer.parseInt(input);

            if(choice.equals(0)){
                if(res.isEmpty()&&optional){
                    flag = false;
                } else if(res.isEmpty()){
                    JOptionPane.showMessageDialog(null,"You must choose at least one!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else if(choice.equals(100)&&!res.isEmpty()){
                res.remove(res.size()-1);
            } else if(choice.equals(200)){
                res.clear();
            } else {
                if(!res.contains(choice)) {
                    res.add(choice-1);
                    if(single) flag = false;
                } else {
                    JOptionPane.showMessageDialog(null,"You must choose a different -", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        clientController.pick(choiceId, res);
    }

    @Override
    public void chooseWeapon(List<WeaponView> weapon, boolean single, boolean optional, String description, String choiceId) {
        game.getOutputBox().writeOnOutput(description);

        String[] names = new String[100];
        int i = 0;
        for(WeaponView we : weapon){
            names[i] = we.name();
            i++;
            game.getOutputBox().writeOnOutput(i + ". " + we.name() + "\n");
        }
        names[i] = "Stop";
        i++;
        names[i] = "Rollback";
        i++;
        names[i] = "Reset";
        List<Integer> res = new ArrayList<>();
        Integer choice;
        boolean flag = true;
        while(flag){

            String input  = JOptionPane.showInputDialog(description);

            while(!input.matches(" ?\\d+")){
                JOptionPane.showMessageDialog(null, "You must choose one number from the ones showed.", "ERROR", JOptionPane.ERROR_MESSAGE);
                input  = JOptionPane.showInputDialog("Riprova e scegli un'arma.");
            }
            choice = Integer.parseInt(input);

            if(choice.equals(0)){
                if(res.isEmpty()&&optional){
                    flag = false;
                } else if(res.isEmpty()){
                    JOptionPane.showMessageDialog(null,"You must choose at least one Weapon!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else if(choice.equals(200)&&!res.isEmpty()){
                res.remove(res.size()-1);
            } else if(choice.equals(100)){
                res.clear();
            } else {
                if(!res.contains(choice)) {
                    res.add(choice-1);
                } else {
                    JOptionPane.showMessageDialog(null,"You must choose a different Weapon", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        clientController.pick(choiceId, res);
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
    public void onStarting(String map, GameMode mode) {
        phase = PhaseType.GAME;
        changeFrameFlag = true;
        waitingScreen.dispatchEvent(new WindowEvent(waitingScreen, WindowEvent.WINDOW_CLOSING));


    }

    @Override
    public void onMessage(Message message) {
        for(String str : message.getChanges()){
            game.getOutputBox().writeOnOutput(str);
        }
    }

    @Override
    public void onLostTurn(Player player) {
        game.getOutputBox().writeOnOutput(player.getUsername() + "lost his turn!");
    }

    @Override
    public void onRespawn() {
        if(game!=null) game.getOutputBox().writeOnOutput("You're respawing now...");
    }

    @Override
    public void onRollback() {
        game.getOutputBox().writeOnOutput("Rollback executed.");

    }

    @Override
    public void onTakeback() {
        game.getOutputBox().writeOnOutput("You can use a Takeback Granade now!");

    }

    @Override
    public void onTerminator() {
        game.getOutputBox().writeOnOutput("You can move the Terminator now!");

    }

    @Override
    public void onCredits() {
        //todo write this method
    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoints) {
        //todo write this method
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
