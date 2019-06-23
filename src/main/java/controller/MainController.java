package controller;

import actions.effects.Effect;
import board.GameMap;
import board.Tile;
import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import gamemanager.Scoreboard;
import grabbables.PowerUp;
import network.Database;
import network.NetworkBuilder;
import network.Player;
import network.ServerInterface;
import player.Actor;
import uid.DamageableUID;
import uid.TileUID;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MainController {

    private static final int TIME_BEFORE_STARTING = ParserConfiguration.parseInt("TimeBeforeStarting");
    private static final int MIN_PLAYER = 1;// ParserConfiguration.parseInt("minNumOfPlayers");
    private static final int MAX_PLAYER = ParserConfiguration.parseInt("maxNumOfPlayers");

    public int timeoutTime;

    private int numOfPlayer;

    private Timer timerForStarting;
    private boolean timerRunning = false;
    private boolean gameStarted = false;
    private Scoreboard scoreboard;

    private GameMap gameMap;
    private GameBuilder game;

    /**
     * The list of slave controllers, in the order of their turns, randomised from slaveMap
     * values at game creation.
     */
    private List<SlaveController> slaveControllerList;
    private Map<DamageableUID, SlaveController> slaveMap;
    private boolean firstRoundOver;

    public SlaveController getSlaveByUID(DamageableUID uid){
        return slaveMap.get(uid);
    }

    MainController(){
        slaveControllerList = new ArrayList<>();
        timeoutTime = 30;
        slaveMap = new HashMap<>(MAX_PLAYER);
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
        return numOfPlayer<MAX_PLAYER && !gameStarted;
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
        notifyDisconnection(numOfPlayer, player);
        if(numOfPlayer < MIN_PLAYER) {
            if(timerRunning)
                timerClose();
            if(gameStarted)
                endGame();
        }
    }

    private void notifyConnection(int num, Player player){
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onConnection(player, num);
        System.out.println(player);
    }

    private void notifyDisconnection(int numOfPlayer, Player player) {
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onDisconnection(player, numOfPlayer);
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

    private void notifyWinner(String winner, int winnerPoints) {
        for(SlaveController slaveController : slaveControllerList)
            slaveController.onWinner(winner, winnerPoints);
    }

    private void startGame() {

        gameStarted = true;
        createGame();

        this.scoreboard = game.getScoreboard();
        this.gameMap = game.getMap();

        for (SlaveController i: slaveControllerList){
            slaveMap.put(i.getSelf().pawnID(), i);
        }
        notifyStarting(game.getMapName());
        //todo notify inizio gioco

        slaveControllerList = new ArrayList<>(slaveMap.values());
        Collections.shuffle(slaveControllerList);
        slaveControllerList.sort((a,b)-> {
            if (a.getSelf().getFirstPlayer())
                return 1;
            if (b.getSelf().getFirstPlayer())
                return -1;
            return 0;
        });

        this.firstRoundOver=false;

        slaveControllerList.get(0).startMainAction();
    }

    private void createGame() {
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
     * Will be called by the EndTurn as long as the flag firstRoundOver is set to false
     * @param going
     * @param next
     */
    private void firstRound(SlaveController going, List<SlaveController> next){
        if (next.isEmpty()){
            firstRoundOver=true;
        }
        startRespawn(
                List.of(going.getSelf().pawnID()),
                2,
                going::startMainAction
        );
    }

    /**
     * Called only as the last statement of a new thread
     *
     * @param responsible the SlaveController which started it all (redundant)
     * @param effects (the list of effects)
     * @param onResolved (When I finish the list of effects run this function)
     *                   If I call a slave controller function in between then I will have to
     *                   call it and pass a runnable that will:
     */
    public void resolveEffect(SlaveController responsible, List<Effect> effects,
                       Runnable onResolved){
        Actor thisActor = responsible.getSelf();
        if (effects.isEmpty()){
            for (Actor a: slaveControllerList.stream()
                    .filter(i->!responsible.equals(i))
                    .map(i->i.getSelf()).collect(Collectors.toList())){

                if (a.removeDamager(thisActor)){ // true if actor was present

                    SlaveController aController = slaveMap.get(a.pawnID());
                    Consumer<Optional<PowerUp>> consumerTagback = opt -> {
                        if (opt.isEmpty()){
                            new Thread(()->resolveEffect(responsible,effects,onResolved)).start();
                        } else {
                            a.discardPowerUp(opt.get());
                            thisActor.addMark(a, 1);
                            broadcastEffectMessage(String.format(
                                    "%s ha usato la granata venom contro %s",
                                    a.pawn().getUsername(),
                                    thisActor.pawn().getUsername()
                            ));
                            new Thread(()->resolveEffect(responsible,effects,onResolved)).start();
                        }
                    };

                    aController.startTagback(thisActor, consumerTagback); //TODO: double check
                    return; // The resolution will be restarted by the tagBack
                }
            }
            new Thread(onResolved).start(); // only if no actor
        } else {
            Effect first = effects.get(0);
            List<Effect> next = effects.subList(1, effects.size());
            Runnable nextOnRes = () -> MainController.this.resolveEffect(responsible, next, onResolved);
            (new Thread(() -> first.mergeInGameMap(responsible, nextOnRes,
                    this::broadcastEffectMessage))).start();
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

        List<SlaveController> nextnext=List.of();
        if (!firstRoundOver){
            nextnext = slaveControllerList.subList(Math.max(currIndex+2, size),size);
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
        getGameMap().refill();
        List<DamageableUID> dead = new ArrayList<>();

        // TODO: iterate first on domination points somehow or else the game will break
        for (DamageableUID uid: getGameMap().getDamageable()){
            if (getGameMap().getPawn(uid).getActor().endTurn(lastPlayed, scoreboard)){
                dead.add(uid); //TODO: do they count as dead if they didn't spawn yet?
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

        Runnable nextAction = next::startMainAction;

        if (!firstRoundOver){ // If the first round isn't over
            Function<List<SlaveController>, Runnable> runnableFunction = nextLis -> () -> firstRound(next, nextLis);
            nextAction = runnableFunction.apply(nextnext);
        }

        this.startRespawn(dead, 1, nextAction); // LAST LINE OF THE FUNCTION
        //TODO: handle first turn of each player differently
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

        slaveMap.get(head).getSelf().drawPowerUpRaw(cards);

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
                    respawnedActor.discardPowerUp(powerUp);
                    respawnedActor.pawn().move(destination);
                    startRespawn(tail, cards, onAllRespawned);
                };
        slaveMap.get(head).startRespawn(onRespawned);
    }

    /**
     * Called to count
     */
    void endGame() {
        Actor winner = scoreboard.claimWinner();
        notifyWinner(winner.pawn().getUsername(), winner.getPoints());
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    private void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
}
