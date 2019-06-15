package testcontroller;

import actions.effects.Effect;
import board.GameMap;
import board.Tile;
import gamemanager.GameBuilder;
import gamemanager.Scoreboard;
import grabbables.PowerUp;
import network.Database;
import network.Player;
import network.ServerInterface;
import player.Actor;
import uid.DamageableUID;
import uid.TileUID;

import javax.swing.plaf.ActionMapUIResource;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class MainController {

    private int numOfPlayer;

    private Timer timerForStarting;
    private boolean timerRunning = false;
    private boolean gameStarted = false;
    private Scoreboard scoreboard;

    private GameMap gameMap;

    private List<SlaveController> slaveControllerList;
    private Map<DamageableUID, SlaveController> slaveMap;

    MainController(){
        slaveControllerList = new ArrayList<>();
        slaveMap = new HashMap<>(5);
    }

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
        //TODO: allow reconnection on crash (could be a token such as username|hash(password)
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
                endGame();
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
        SlaveController slave = new SlaveController(this, player, network);
        slaveControllerList.add(slave);
        return slave;
    }

    public static void main(String[] str) {
        MainController initGame = new MainController();
        NetworkBuilder network = new NetworkBuilder(initGame);
    }

    /**
     *
     * @param responsible the SlaveController which started it all (redundant)
     * @param effects (the list of effects)
     * @param onResolved (When I finish the list of effects run this function)
     *                   If I call a slave controller function in between then I will have to
     *                   call it and pass a runnable that will:
     */
    void resolveEffect(SlaveController responsible, List<Effect> effects,
                       Runnable onResolved){
        if (effects.isEmpty()){
            new Thread(onResolved).start();
        } else {
            Effect first = effects.get(0);
            List<Effect> next = effects.subList(1, effects.size());
            Runnable nextOnRes = () -> MainController.this.resolveEffect(responsible, next, onResolved);
            broadcastEffectMessage(first.effectString(responsible.getSelf()));
            (new Thread(() -> first.mergeInGameMap(responsible, nextOnRes))).start();
        }
    }

    /**
     *
     * @param lastPlayed the player whose turn just ended
     */
    public void endTurn(Player lastPlayed){
        /*
        0. Riempie la mappa
        1. Prende tutti i domination points
        2. Per ognuno se hai fatto danno allora aggiunge un segnalino del colore di Player alla
        gameBoard e se il player è nella cella gli fa uno di danno (metodi di DominationPointActor)
        3. Prende tutti i pawn morti
        4. Crea thread e respawna in ordine tutti
        5. Alla fine il thread controlla con Scoreboard che non sia iniziata la frenesia finale
            5a. Frenesia iniziata: Chiama Main.finalFrenzy(nextPlayer)
            5b. Inizia il main line sul prossimo turno
         */
    }

    public void finalFrenzy(Player next){
        /*
        1. Chiama su tutti gli actor: finalFrenzyBegin

         */

    }

    public GameMap getGameMap() {
        return gameMap;
    }

    private void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}
