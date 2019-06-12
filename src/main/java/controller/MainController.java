package controller;

import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import network.Database;
import network.NetworkBuilder;
import network.Player;
import player.Actor;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainController {

    private int numOfPlayer;

    private Timer timerForStarting;
    private boolean timerRunning = false;

    private boolean gameStarted = false;

    private static final int TIME_BEFORE_STARTING = ParserConfiguration.parseInt("TimeBeforeStarting");
    private static final int MIN_PLAYER = ParserConfiguration.parseInt("minNumOfPlayers");
    private static final int MAX_PLAYER = ParserConfiguration.parseInt("maxNumOfPlayers");

    private GameBuilder game;


    public MainController(){
        numOfPlayer = 0;
    }


    private void checkGameStart() {
        if(numOfPlayer < MIN_PLAYER || gameStarted)
            return;
        if(numOfPlayer == MAX_PLAYER) {
            timerClose();
            startGame();
        }
        if(numOfPlayer<MAX_PLAYER && numOfPlayer>=MIN_PLAYER && !timerRunning){
            timerStart(TIME_BEFORE_STARTING);
            notifyTimer(TIME_BEFORE_STARTING);
        }
    }

    public SlaveController connect(Player player) {
        if(!canConnect())   throw new IllegalArgumentException("Invalid request");

        numOfPlayer++;
        System.out.println("Connection");
        notifyConnection(numOfPlayer, player);
        checkGameStart();

        return new SlaveController(player);
    }

    public void reconnect(Player player) {
        numOfPlayer++;
        System.out.println("Reconnection");
        notifyConnection(numOfPlayer, player);
        checkGameStart();
    }


    public boolean canConnect() {
        return numOfPlayer<5 && !gameStarted;
    }

    public void logout(Player player) {
        numOfPlayer--;
        notifyDisconnection(player);
        if(numOfPlayer < MIN_PLAYER) {
            if(timerRunning) {
                timerClose();
            }
            else {
                terminateGame();
            }
        }
    }

    void notifyConnection(int n, Player player){
        System.out.println(player);
    }

    void notifyDisconnection(Player player) {
        System.out.println("size\t"+Database.get().getConnectedTokens().size());
        System.out.println("Disconnection of\n" + player);
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
        notifyStarting();
        gameStarted = true;
        createGame();
    }

    private GameBuilder createGame() {
        try {
            game = new GameBuilder(numOfPlayer);
            Iterator<Actor> actorList = game.getActorList().iterator();
            for(Player player : Database.get().getPlayers()) {
                Actor actor = actorList.next();
                player.setActor(actor);
                player.setUid(actor.pawnID());
                actor.pawn().setUsername(player.getUsername());
                actor.pawn().setColor((Color) Color.class.getField(player.getColor()).get(null));
            }
        }
        catch (FileNotFoundException | NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
        return game;
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

        MainController initGame = new MainController();
        NetworkBuilder network = new NetworkBuilder(initGame);
    }
}
