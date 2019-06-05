package controller;

import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import network.NetworkBuilder;
import network.Player;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;


public class InitGame {

    private int numOfPlayer;
    private Timer timerForStarting;
    private boolean timerRunning = false;
    private static final int TIME_BEFORE_STARTING = ParserConfiguration.parseInt("TimeBeforeStarting");
    private static final int MIN_PLAYER = ParserConfiguration.parseInt("minNumOfPlayers");
    private static final int MAX_PLAYER = ParserConfiguration.parseInt("maxNumOfPlayers");

    private GameBuilder game;


    public InitGame(){
        numOfPlayer = 0;
    }


    private void checkGameStart() {
        if(numOfPlayer < MIN_PLAYER)
            return;
        if(numOfPlayer == MAX_PLAYER) {
            timerClose();
            notifyStarting();
            startGame();
        }
        if(numOfPlayer<MAX_PLAYER && numOfPlayer>=MIN_PLAYER && !timerRunning){
            timerStart(TIME_BEFORE_STARTING);
            notifyTimer(TIME_BEFORE_STARTING);
        }
    }

    public void connect(Player player) {
        //todo: return slave controller
        numOfPlayer++;
        System.out.println("Connection by\t" + player);
        //notifyConnection(numOfPlayer, player.getUsername(), player.getColor());
        checkGameStart();
    }

    public void reconnect(Player player) {
        //todo: return slave controller
        numOfPlayer++;
        System.out.println("Connection by\t" + player);
        //notifyConnection(numOfPlayer, player.getUsername(), player.getColor());
        checkGameStart();
    }



    public void logout(Player player) {
        numOfPlayer--;
        System.out.println("Logout by\t" + player);
        if(numOfPlayer < MIN_PLAYER) {
            if(timerRunning) {
                timerClose();
            }
            else {
                terminateGame();
            }
        }
    }

    void notifyConnection(int n, String user, String color){
        //todo
    }

    void notifyStarting(){
        //todo
    }

    void notifyTimer(int ms){
        //todo
    }


    void terminateGame(){
        //todo
    }

    public void startGame() {
        try {
            game = new GameBuilder(numOfPlayer);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void timerClose(){
        timerForStarting.cancel();
        timerRunning = false;
    }

    private void timerStart(int time){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("si parte");
                startGame();
                timerClose();
            }
        };
        timerForStarting = new Timer("Timer");
        timerForStarting.schedule(task, time);
        timerRunning = true;
    }

    public static void main(String[] str) {
        InitGame initGame = new InitGame();
        NetworkBuilder network = new NetworkBuilder(initGame);
    }
}
