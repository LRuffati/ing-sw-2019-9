package testcontroller;

import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import network.Database;
import network.NetworkBuilder;
import network.Player;
import network.ServerInterface;
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
    private List<SlaveController> slaveControllerList;


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

    /**
     * Method called by the network layer when a new connection is made.
     * It notifies all the clients that there is a new player,
     * and then it checks whether the game can start, and in case it creates the new game.
     * This method can be used only if the game is not started yet.
     * @param player The Player that logged in
     */
    public void connect(Player player) {
        if(!canConnect())   throw new IllegalArgumentException("Invalid connection request");

        numOfPlayer++;
        System.out.println("Connection");
        notifyConnection(numOfPlayer, player);
        checkGameStart();
    }

    /**
     * Method called by the network layer when a reconnection is made.
     * It notifies all the clients that the player is back online,
     * and then it checks whether the game can start, and in case it creates the new game
     * @param player The Player that logged in
     */
    public void reconnect(Player player) {
        numOfPlayer++;
        System.out.println("Reconnection");
        notifyConnection(numOfPlayer, player);
        checkGameStart();
    }


    /**
     * @return True iif the game is not started and the number of player is below the maximum
     */
    public boolean canConnect() {
        return numOfPlayer<5 && !gameStarted;
    }

    /**
     * Method called by the network layer when a logout is made.
     * It notifies all the clients that the player is not playing.
     * If the number of playeris below the minimum (3 player by default)
     * it resets the Timer that controls the time before starting and terminates the game if started.
     * @param player The Player that logged out
     */
    public void logout(Player player) {
        numOfPlayer--;
        notifyDisconnection(player);
        if(numOfPlayer < MIN_PLAYER) {
            if(timerRunning)
                timerClose();
            if(gameStarted)
                terminateGame();
        }
    }

    private void notifyConnection(int n, Player player){
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onConnection(player);
        System.out.println(player);
    }

    private void notifyDisconnection(Player player) {
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onDisconnection(player);
        System.out.println("Disconnection of\n" + player);
    }

    private void notifyStarting(String map){
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onStarting(map);
    }

    private void notifyTimer(int ms){
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onTimer(ms);
    }

    private void terminateGame(){
        //todo
    }

    private void startGame() {
        gameStarted = true;
        createGame();
        notifyStarting(game.getMapName());
        //todo notify inizio gioco
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


    /**
     * This function is called by the network layer upon receiving the first connection from a User
     * @return the SlaveController all further calls by the client need to be addressed to
     */
    public SlaveController bind(Player player, ServerInterface network){
        SlaveController slave = new SlaveController(player, network);
        slaveControllerList.add(slave);
        return slave;
    }



    public static void main(String[] str) {
        MainController initGame = new MainController();
        NetworkBuilder network = new NetworkBuilder(initGame);
    }
}
