package view.cli;

import actions.utils.AmmoColor;
import controller.Message;
import controller.controllerclient.ClientControllerClientInterface;
import view.View;
import network.Player;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CLIDemo implements View {
    private static CLIMap climap;
    private Scanner in = new Scanner(System.in);
    private ClientControllerClientInterface client;
    private boolean inputTake = true;
    private CommandParser commandParser;
    private Thread scanThread;

    String pickStringMessage;
    private List<Integer> chosenList = new ArrayList<>();
    private List<Integer> toReturn;

    /**
     * To be called when the server starts the game. It generates the map (with everything included on it).
     */

    public CLIDemo(ClientControllerClientInterface client){
        this.client = client;
        this.commandParser = new CommandParser(this);
        this.scanThread = new Thread(()->{
            while (inputTake) {
                String string = in.next();
                commandParser.parseCommand(string);
            }
        });
        greetings();
    }

    /**
     * Method to be called from other classes. Intended to make the CLIMap class not called from other classes.
     */
    public void getPrintedMap(){
        climap.printMap();
    }

    /**
     * Method to introduce a new player to the game and show the initial options.
     * It initially clear the whole command line, then it shows the title of the game.
     */
    void greetings(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n  /$$$$$$  /$$$$$$$  /$$$$$$$  /$$$$$$$$ /$$   /$$  /$$$$$$  /$$       /$$$$$$ /$$   /$$ /$$$$$$$$\n" +
                " /$$__  $$| $$__  $$| $$__  $$| $$_____/| $$$ | $$ /$$__  $$| $$      |_  $$_/| $$$ | $$| $$_____/\n" +
                "| $$  \\ $$| $$  \\ $$| $$  \\ $$| $$      | $$$$| $$| $$  \\ $$| $$        | $$  | $$$$| $$| $$      \n" +
                "| $$$$$$$$| $$  | $$| $$$$$$$/| $$$$$   | $$ $$ $$| $$$$$$$$| $$        | $$  | $$ $$ $$| $$$$$   \n" +
                "| $$__  $$| $$  | $$| $$__  $$| $$__/   | $$  $$$$| $$__  $$| $$        | $$  | $$  $$$$| $$__/   \n" +
                "| $$  | $$| $$  | $$| $$  \\ $$| $$      | $$\\  $$$| $$  | $$| $$        | $$  | $$\\  $$$| $$      \n" +
                "| $$  | $$| $$$$$$$/| $$  | $$| $$$$$$$$| $$ \\  $$| $$  | $$| $$$$$$$$ /$$$$$$| $$ \\  $$| $$$$$$$$\n" +
                "|__/  |__/|_______/ |__/  |__/|________/|__/  \\__/|__/  |__/|________/|______/|__/  \\__/|________/");

        System.out.println("Welcome to ADRENALINE!\n");

    }

    /**
     * It takes username and password as input if the player had already joined the game with the same credentials.
     * It also takes a color if it's the player's first login.
     * @return true if the player joined the game.
     */
    private void joinGame(){
        String username = "";
        String password = "";
        String color = "";

        while(username.isEmpty()){
            System.out.println(">>> Enter your username!");
            username = in.nextLine();
        }
        while(password.isEmpty()){
            System.out.println(">>> Enter your password!");
            password = in.nextLine();
        }
        while(color.isEmpty()){
            System.out.println(">>> Choose your color:\n -> Gray\n -> Pink\n -> Yellow\n -> Green\n -> Blue\n -> Type 'y' " +
                    "if you've already picked a color in a previous login");
            color = in.nextLine();
            if(color.equalsIgnoreCase("y")) {
                client.login(username,password);
                return;
            }
            if(!color.equalsIgnoreCase("gray")&&!color.equalsIgnoreCase("pink")&&!color.
                    equalsIgnoreCase("yellow")&&!color.equalsIgnoreCase("green")&&!color.
                    equalsIgnoreCase("blue")){
                System.out.println("Invalid color. Pick a color among the followings:");
                color = "";
            }
        }
        client.login(username,password,color);
    }


    /**
     * After the player joined the game, he await for the game to start. This can happen only when there are three or
     * more players in the same game and the firstPlayer decides do start or the timer elapses.
     * @param timeLeft is the timer (in seconds) that starts when there are three players that joined the game.
     */
    private void waitForStart(int timeLeft){
        System.out.println("Wait for other players to join the game.");
    }

    void quitGame(){
        System.out.println("Are you sure you want to quit the game? Press 'y' if you want to proceed, 'n' if you" +
                "want to go back.");
        if(in.nextLine().equalsIgnoreCase("y")){
            client.quit();
            endGame();
        } else if (in.nextLine().equalsIgnoreCase("n")){
            client.rollback();
        }
    }


    void printAppliedTarget(List<TargetView> targetViewList){
        climap.applyTarget(targetViewList);

    }

    private void choicer(boolean optional, boolean single, String choiceId, String type, String str){
        int n;
        try {
            n = Integer.parseInt(str);
        }
        catch (NumberFormatException e){
            return;
        }

        switch (n) {
            case 0:
                if(chosenList.isEmpty() && !optional) {
                    System.out.println("You must choose at least one " + type + ".");
                    break;
                }

                pickStringMessage = "";

                List<Integer> toReturn = new ArrayList<>(chosenList);
                chosenList.clear();

                System.out.print("Chosen option(s) ");
                for(Integer i : toReturn) System.out.println(i + " ");
                System.out.println();
                client.pick(choiceId, toReturn.stream().map(x -> x-1).collect(Collectors.toList()));
                break;

            case 99:
                if(!chosenList.isEmpty()) chosenList.remove(chosenList.size()-1);
                break;

            case 100:
                chosenList.clear();
                client.restartSelection();
                break;

            case(200):
                chosenList.clear();
                client.rollback();
                break;

            default:
                System.out.println("adding" + n);
                chosenList.add(n);
                if(single){
                    toReturn = new ArrayList<>(chosenList);
                    chosenList.clear();
                    System.out.print("Chosen option(s) ");
                    for(Integer i : toReturn) System.out.println(i + " ");
                    System.out.println();
                    client.pick(choiceId, toReturn.stream().map(x -> x-1).collect(Collectors.toList()));
                }
                break;
        }
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void chooseTarget(List<TargetView> target, boolean single, boolean optional, String description, GameMapView gameMap, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();

        CLIMap map = new CLIMap(gameMap);
        map.applyTarget(target);
        //TODO: senza questo c'è comunque una stampa. serve?
        //eventualmente toglierne una sopra?
        printAppliedTarget(target);
        builder.append("Choose your target(s):\n0. Exit Selection\n");
        Iterator<TargetView> targetIterator = target.iterator();
        int i = 1;
        while(targetIterator.hasNext()){
            TargetView tw = targetIterator.next();
            Collection<DamageableUID> dmg = tw.getDamageableUIDList();
            Collection<TileUID> tile = tw.getTileUIDList();
            if(dmg!=null){
                for(ActorView a: gameMap.players()){
                    if(a.uid().equals(dmg.iterator().next())){
                        builder.append(a.getAnsi());
                        builder.append(i);
                        builder.append(". ");
                        builder.append(a.name());
                        builder.append(" whom character on the map is '");
                        builder.append(climap.getPlayers().get(a));
                        builder.append("'.\n");
                        i+=1;
                        break;
                    }
                }
            } else {
                for(TileView t : gameMap.allTiles()){
                    if(t.uid().equals(tile.iterator().next())){
                        builder.append(t.getAnsi());
                        builder.append(i);
                        builder.append(". ");
                        builder.append(gameMap.getCoord(t).toString());
                        builder.append("\n");
                        i+=1;
                        break;
                    }
                }
            }
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "target", string);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void chooseAction(List<ActionView> action, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        builder.append("Choose your action(s):\n0. Exit selection\n");
        Iterator<ActionView> actionIterator = action.iterator();
        int i = 1;
        while(actionIterator.hasNext()){
            builder.append(i);
            builder.append(". ");
            builder.append(actionIterator.next().getName());
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);


        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "action", string);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void chooseWeapon(List<WeaponView> weapon, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder toChoose = new StringBuilder();
        toChoose.append("Choose your weapons:\n0. Exit selection");

        Iterator<WeaponView> weaponIterator = weapon.iterator();
        int i = 1;
        while(weaponIterator.hasNext()){
            WeaponView wv = weaponIterator.next();
            toChoose.append(i);
            toChoose.append(". ");
            toChoose.append(wv.name());
            toChoose.append("\n");
            i+=1;
        }
        toChoose.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");

        pickStringMessage = toChoose.toString();
        System.out.println(pickStringMessage);

        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "weapon", string);
        commandParser.bind(consumer);
    }

    @Override
    public void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        builder.append("Choose your PowerUp(s):\n0. Exit selection\n");
        Iterator<PowerUpView> puIterator = powerUp.iterator();
        int i = 1;
        while(puIterator.hasNext()){
            PowerUpView pu = puIterator.next();
            builder.append(i);
            builder.append(". ");
            if(pu.ammo().toColor().equals(Color.RED))
                builder.append("\u001B[31m");
            if(pu.ammo().toColor().equals(Color.YELLOW))
                builder.append("\u001B[33m");
            if(pu.ammo().toColor().equals(Color.BLUE))
                builder.append("\u001B[34m");
            builder.append(pu.type().toString());
            builder.append("\u001B[0m \n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "power up", string);

        commandParser.bind(consumer);
    }

    @Override
    public void chooseString(List<String> string, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        builder.append("Choose an option:\n0. Exit selection\n");
        Iterator<String> strIterator = string.iterator();
        int i = 1;
        while(strIterator.hasNext()){
            String str = strIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(str);
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        Consumer<String> consumer =
                stringa -> choicer(optional, single, choiceId, "string", stringa);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void onRollback() {
        System.out.println("Rollback executed.");
    }

    @Override
    public void onMessage(Message message) {
        for(String str : message.getChanges()){
            System.out.println(str);
        }
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void terminated() {
        System.out.println("Action finally executed.");
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void updateMap(GameMapView gameMapView) {
        climap = new CLIMap(gameMapView);
        climap.printMap();
    }

    @Override
    public void loginResponse(boolean result, boolean invalidUsername, boolean invalidColor) {
        if(result){
            System.out.println("Login completed correctly.");
        } else {
            if(invalidColor) {
                System.out.println("Sorry! This color has already been picked from another player. If" +
                        "you're that player, try login with your old credentials.");
            } else {
                if(invalidUsername){
                    System.out.println("Please, try another username and/or password.");
                }
            }
            joinGame();
        }
    }

    @Override
    public void loginNotif() {
        joinGame();
    }

    @Override
    public void onConnection(Player player, boolean connected, int numOfPlayer) {
        if(connected){
            System.out.println("Player " + player.getUsername() + " just logged in! There are now " + numOfPlayer
            + " in the game");
        } else {
            System.out.println("Player " + player.getUsername() + " just logged out! There are now " + numOfPlayer
                    + " in the game");
        }
    }

    @Override
    public void onStarting(String map) {
        System.out.println("The game is goin' to start in a moment...");
        scanThread.start();
    }

    @Override
    public void onTimer(int timeToCount) {
        System.out.println("From now on you have " + timeToCount + " seconds to choose.");
        waitForStart(timeToCount);
    }

    @Override
    public void onRespawn() {
        System.out.println("You're respawing now...");
    }

    @Override
    public void onTakeback() {
        System.out.println("You can use a Takeback Granade now!");
    }

    @Override
    public void onTerminator() {
        System.out.println("You can move the Terminator now!");
    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoints) {
        System.out.println(String.format("Game is over!%nThe winner is...%n%s%nHe won with %d points, you had %d points", winner, winnerPoints, yourPoints));
    }

    @Override
    public void onCredits() {
        System.out.println("Adrenaline™ is a game by Group9\nLorenzo Ruffati\nCarmelo Sarta\nPietro Tenani");
        endGame();
    }

    /**
     * It shows the room which the tile belongs. If there are weapons or ammoTile and finally what players are in this
     * tile.
     * @param t to get the info from.
     */
    private void tileInfo(TileView t){
        System.out.print("\n>> The tile belongs to the ");
        System.out.print(t.getAnsi() + t.getColorName() + "\u001B[0m" + " room\n");
        if(t.spawnPoint()){
            System.out.println(">> There is a spawn point for weapons in the tile.\n");
            System.out.println(">> You can pick up: ");
            for(WeaponView w : t.weapons()){
                System.out.println(" +" + w.name());
            }
        } else {
            System.out.println(">> There is a spawn point for the following ammunition in the tile:\n");
            System.out.println("+ Number of Red: " + t.ammoCard().numOfRed());
            System.out.println("+ Number of Blue: " + t.ammoCard().numOfBlue());
            System.out.println("+ Number of Yellow: " + t.ammoCard().numOfYellow());
        }
        int i = 0;
        if(t.players().isEmpty()){
            System.out.println(">> There are no players in the tile.");
        } else {
            System.out.println(">> The following players are in the tile: ");
            for(ActorView a: t.players()){
                System.out.println(i + ". " + a.getAnsi() + a.name() + "\u001B[0m \n");
                i++;
            }
        }
    }

    private void playerInfo(ActorView player) {
        System.out.println("\n>> The player " + player.getAnsi() + player.name() + "\u001B[0m" + " still got " +
                (player.getHP()-player.damageTaken().size()) + "HP left.");
        System.out.println("\n>> He's got the following ammo: ");
        System.out.println(player.ammo().get(AmmoColor.RED) + "\tRED");
        System.out.println(player.ammo().get(AmmoColor.BLUE) + "\tBLUE");
        System.out.println(player.ammo().get(AmmoColor.YELLOW) + "\tYELLOW");
        System.out.println("\n>> He's got the following loaded weapons: ");
        int i = 0;
        for(WeaponView w : player.getLoadedWeapon()){
            System.out.println(i + ". " + w.name());
            i++;
        }
        System.out.println("\n>> He's got the following unloaded weapons: ");
        for(WeaponView w : player.getUnloadedWeapon()){
            System.out.println(i + ". " + w.name());
            i++;
        }
        System.out.println("\n>> He's got the following powerUps: ");
        for(PowerUpView w : player.getPowerUp()){
            System.out.println(i + ". " + w.type());
            i++;
        }
    }

    void askInfo() {
        clearScreen();
        getPrintedMap();
        System.out.println(">> 1. Players");
        System.out.println(">> 2. Tiles");
    }

    void askPlayer() {
        int i = 0;
        for (Map.Entry<ActorView, Character> actor : climap.getPlayers().entrySet()) {
            System.out.println(i + ". " + actor.getKey().name());
            i++;
        }
    }

    void choosePlayer(String str) {
        int value = Integer.parseInt(str);
        int i = 0;
        for(Map.Entry entry : climap.getPlayers().entrySet()) {
            if(i == value) {
                playerInfo((ActorView)entry.getKey());
            }
            i++;
        }
    }

    void askTile() {
        int i = 0;
        for(TileView tile : climap.getMp().allTiles()) {
            System.out.println(i + ". " + climap.getMp().getCoord(tile));
            i++;
        }
    }

    void chooseTile(String str) {
        int value = Integer.parseInt(str);
        int i = 0;
        for(TileView tile : climap.getMp().allTiles()) {
            if(i == value) {
                tileInfo(tile);
            }
            i++;
        }
    }

    void endGame(){
        System.out.println("$$$$$$$$\\ $$\\                           $$\\             $$\\     $$\\                          $$$$$$\\                           $$$$$$$\\  $$\\                     $$\\                     \n" +
                "\\__$$  __|$$ |                          $$ |            \\$$\\   $$  |                        $$  __$$\\                          $$  __$$\\ $$ |                    \\__|                    \n" +
                "   $$ |   $$$$$$$\\   $$$$$$\\  $$$$$$$\\  $$ |  $$\\        \\$$\\ $$  /$$$$$$\\  $$\\   $$\\       $$ /  \\__|$$$$$$\\   $$$$$$\\        $$ |  $$ |$$ | $$$$$$\\  $$\\   $$\\ $$\\ $$$$$$$\\   $$$$$$\\  \n" +
                "   $$ |   $$  __$$\\  \\____$$\\ $$  __$$\\ $$ | $$  |        \\$$$$  /$$  __$$\\ $$ |  $$ |      $$$$\\    $$  __$$\\ $$  __$$\\       $$$$$$$  |$$ | \\____$$\\ $$ |  $$ |$$ |$$  __$$\\ $$  __$$\\ \n" +
                "   $$ |   $$ |  $$ | $$$$$$$ |$$ |  $$ |$$$$$$  /          \\$$  / $$ /  $$ |$$ |  $$ |      $$  _|   $$ /  $$ |$$ |  \\__|      $$  ____/ $$ | $$$$$$$ |$$ |  $$ |$$ |$$ |  $$ |$$ /  $$ |\n" +
                "   $$ |   $$ |  $$ |$$  __$$ |$$ |  $$ |$$  _$$<            $$ |  $$ |  $$ |$$ |  $$ |      $$ |     $$ |  $$ |$$ |            $$ |      $$ |$$  __$$ |$$ |  $$ |$$ |$$ |  $$ |$$ |  $$ |\n" +
                "   $$ |   $$ |  $$ |\\$$$$$$$ |$$ |  $$ |$$ | \\$$\\           $$ |  \\$$$$$$  |\\$$$$$$  |      $$ |     \\$$$$$$  |$$ |            $$ |      $$ |\\$$$$$$$ |\\$$$$$$$ |$$ |$$ |  $$ |\\$$$$$$$ |\n" +
                "   \\__|   \\__|  \\__| \\_______|\\__|  \\__|\\__|  \\__|          \\__|   \\______/  \\______/       \\__|      \\______/ \\__|            \\__|      \\__| \\_______| \\____$$ |\\__|\\__|  \\__| \\____$$ |\n" +
                "                                                                                                                                                       $$\\   $$ |              $$\\   $$ |\n" +
                "                                                                                                                                                       \\$$$$$$  |              \\$$$$$$  |\n" +
                "                                                                                                                                                        \\______/                \\______/ ");
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

class CommandParser{

    private Consumer<String> strings;
    private CLIDemo cliDemo;

    private State state;

    CommandParser(CLIDemo cliDemo){
        this.cliDemo = cliDemo;
        this.state = State.MAIN;
    }

    void bind(Consumer<String> strings){
        this.strings = strings;
    }

    void parseCommand(String str){
        System.out.println(str);
        System.out.println(state);
        switch (state) {
            case MAIN:
                if (str.equalsIgnoreCase("info")) {
                    state = State.INFO;
                    cliDemo.askInfo();
                    break;
                }
                if (str.equalsIgnoreCase("quit")) {
                    cliDemo.quitGame();
                    break;
                }
                strings.accept(str);
                /*
                if (strings != null) {
                    strings.accept(str);
                    strings = null;
                }
                */
                break;

            case INFO:
                if (str.equals("1")) {
                    state = State.CHOOSEPLAYER;
                    cliDemo.askPlayer();
                    break;
                }
                if (str.equals("2")) {
                    state = State.CHOOSETILE;
                    cliDemo.askTile();
                    break;
                }
                break;

            case CHOOSEPLAYER:
                cliDemo.choosePlayer(str);
                state = State.MAIN;
                System.out.println(cliDemo.pickStringMessage);
                break;

            case CHOOSETILE:
                cliDemo.chooseTile(str);
                state = State.MAIN;
                System.out.println(cliDemo.pickStringMessage);
                break;

            default:
                System.out.println("type info or quit");
                break;
        }
    }

    private enum State {
        MAIN, INFO, CHOOSEPLAYER, CHOOSETILE
    }
}