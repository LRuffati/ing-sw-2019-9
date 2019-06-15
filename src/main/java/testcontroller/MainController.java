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

    private void startGame() {
        gameStarted = true;
        createGame();
        for (SlaveController i: slaveControllerList){
            slaveMap.put(i.getSelf().pawnID(), i);
        }
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

    private void broadcastEffectMessage(String effectString) {
        for (SlaveController i: slaveControllerList){
            i.addNotification(effectString);
        }
    }

    /**
     *
     * @param lastPlayed the player whose turn just ended
     */
    public void endTurn(Actor lastPlayed){
        boolean frenzyBefore = scoreboard.finalFrenzy();
        SlaveController current = slaveMap.get(lastPlayed.pawnID());
        SlaveController next;
        int currIndex = slaveControllerList.indexOf(current);
        int size = slaveControllerList.size();
        if (currIndex<(size-1)){
            next = slaveControllerList.get(currIndex+1);
        } else {
            next = slaveControllerList.get(0);
        }
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
        List<DamageableUID> dead = new ArrayList<>();
        for (TileUID t: getGameMap().allTiles()){
            getGameMap().getTile(t).endTurn(lastPlayed);
        }
        for (DamageableUID uid: getGameMap().getDamageable()){
            if (getGameMap().getPawn(uid).getActor().endTurn(lastPlayed, scoreboard)){
                dead.add(uid);
            }
        }

        if (scoreboard.finalFrenzy() & current.getSelf().isLastInFrenzy()){
            // It's not necessary to respawn if the game ends anyway
            this.endGame();
            return;
        }

        if (!frenzyBefore & scoreboard.finalFrenzy()){ // If something triggered the FF
            //1. Start finalFrenzy for each pawn
            boolean afterLast = false;
            for (SlaveController i: slaveControllerList){
                // after last is false for all players between the first one and the one who just
                // ended the turn and true for all others
                i.getSelf().beginFF(!afterLast);
                afterLast = afterLast | i.equals(current);
            }
            //2. For current
            current.getSelf().setLastInFrenzy();
        }

        this.startRespawn(dead, 1, next::startMainAction); // LAST LINE OF THE FUNCTION
        return;
    }

    /**
     *
     * @param dead the players which have to be resurrected
     * @param cards the amount of cards to draw. 1 for respawn 2 for first placement
     * @param onAllRespawned The runnable to call when the function is called with an empty list.
     *                      Main use is for it to trigger the "start next turn". It should be not
     *                      blocking, it will not be run in a thread but just called
     */
    private void startRespawn(List<DamageableUID> dead, int cards, Runnable onAllRespawned) {
        /*
        1. Add the given amount of cards to the first one (drawPowerupsraw)
        2. In a thread call respawn on the first slaveController
            a. Provide a consumer that:
                - Discards the powerup
                - Moves pawn to the corresponding tile
                - Calls startRespawn with the tail of dead and same parameter
        3. If dead is empty run onAllRespawned
         */
        if (dead.isEmpty())
            onAllRespawned.run();

        DamageableUID head = dead.get(0);
        List<DamageableUID> tail = dead.subList(1,dead.size());

        slaveMap.get(head).getSelf().drawPowerupRaw(cards);

        Consumer<PowerUp> onRespawned =
                powerUp -> {
                    SlaveController slave = slaveMap.get(head);
                    Actor respawnedActor = slave.getSelf();
                    Set<Tile> spawns =
                            getGameMap().allTiles().stream()
                                    .map(getGameMap()::getTile)
                                    .filter(Tile::spawnPoint)
                                    .collect(Collectors.toSet());
                    TileUID destination = powerUp.spawnLocation(spawns);
                    respawnedActor.discardPowerup(powerUp);
                    respawnedActor.getPawn().move(destination);
                    startRespawn(tail, cards, onAllRespawned);
                };
        slaveMap.get(head).startRespawn(onRespawned);
    }

    /**
     * Called to count
     */
    void endGame(){
        /*
            1. Count points
            2. Declare winner
         */

    }

    public GameMap getGameMap() {
        return gameMap;
    }

    private void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}