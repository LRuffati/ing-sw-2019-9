package view.cli;

import actions.utils.AmmoColor;
import board.Coord;
import controller.GameMode;
import controller.Message;
import controller.controllerclient.ClientControllerClientInterface;
import gamemanager.ParserConfiguration;
import gamemanager.ParserWeapon;
import grabbables.Weapon;
import view.View;
import network.Player;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.*;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CLIDemo implements View {
    private static CLIMap climap;
    private GameMapView gameMapView;
    private Scanner in = new Scanner(System.in);
    private ClientControllerClientInterface client;
    private boolean inputTake = true;
    private CommandParser commandParser;
    private Thread scanThread;

    String pickStringMessage;
    private List<Integer> chosenList = new ArrayList<>();
    private String yourPlayerChar;

    private List<WeaponView> listOfWeapon;

    /**
     * To be called when the server starts the game. It generates the map (with everything included on it).
     */

    public CLIDemo(ClientControllerClientInterface client) {

        listOfWeapon = ParserWeapon.parseWeapons(ParserConfiguration.parsePath("weaponPath"))
                .stream().map(Weapon::generateView2).collect(Collectors.toList());

        this.client = client;
        this.commandParser = new CommandParser(this);
        this.scanThread = new Thread(()->{
            while (inputTake) {
                //todo: verify
                String string = in.nextLine();
                commandParser.parseCommand(string);
            }
        });

        Consumer<String> consumer =
                string -> {};
        commandParser.bind(consumer);

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


    private void pick(String choiceId) {
        pickStringMessage = "";

        List<Integer> toReturn = new ArrayList<>(chosenList);
        chosenList.clear();

        StringBuilder builder = new StringBuilder();
        builder.append("Chosen option(s):\t[ ");
        for(Integer i : toReturn) builder.append(i).append(" ");
        builder.append("]");
        System.out.println(builder.toString());
        client.pick(choiceId, toReturn.stream().map(x -> x-1).collect(Collectors.toList()));
    }

    private void choicer(boolean optional, boolean single, String choiceId, String type, String str, int max){
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
                pick(choiceId);
                break;

            case 99:
                if(!chosenList.isEmpty())
                    chosenList.remove(chosenList.size()-1);
                break;

            case 100:
                chosenList.clear();
                client.restartSelection();
                break;

            case 200:
                chosenList.clear();
                client.rollback();
                break;

            default:
                if(n<0 || n>=max){
                    System.out.println("You must choose a number lower than " + max+1 + "\n");
                } else {
                    chosenList.add(n);
                    if (single) {
                        pick(choiceId);
                    }
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
        System.out.println(description);

        List<Color> colorsOfTargets = List.of(Color.green, Color.yellow, Color.pink, Color.red, Color.blue);
        CLIMap map = new CLIMap(gameMap);
        map.applyTarget(target, colorsOfTargets);
        builder.append("Choose your target(s):\n0. Exit Selection\n");

        Iterator<TargetView> targetIterator = target.iterator();
        int i = 1;
        while(targetIterator.hasNext()) {
            TargetView tw = targetIterator.next();
            List<DamageableUID> dmg = tw.getDamageableUIDList();
            List<TileUID> tile = tw.getTileUIDList();

            if (tw.isDedicatedColor()) {
                builder.append(i);
                builder.append(". ");
                Color col = colorsOfTargets.get(i - 1);
                builder.append("Option ");
                builder.append(AnsiColor.getAnsi(col));
                builder.append(AnsiColor.getColorName(col));
                builder.append(AnsiColor.getDefault());
                builder.append(".\n");
                i += 1;
            } else {

                if (!dmg.isEmpty()) {
                    for(DamageableUID next : dmg) {
                        for (ActorView a : gameMap.players()) {
                            if (a.uid().equals(next)) {
                                builder.append(i);
                                builder.append(". ");
                                builder.append(AnsiColor.getAnsi(a.color()));
                                builder.append(a.name());
                                builder.append(AnsiColor.getDefault());
                                builder.append(" whom character on the map is '");
                                builder.append(climap.getPlayers().entrySet()
                                        .stream().filter(x -> x.getKey().name().equals(a.name()))
                                        .map(Map.Entry::getValue).collect(Collectors.toList()).get(0));
                                builder.append("'.\n");
                                i += 1;
                                break;
                            }
                        }
                        for (Map.Entry<Coord, ActorView> entry : gameMap.dominationPointActor().entrySet()) {
                            if (entry.getValue().uid().equals(next)) {
                                builder.append(i).append(". ");
                                builder.append(AnsiColor.getAnsi(entry.getValue().color())).append(entry.getValue().name()).append(AnsiColor.getDefault());
                                builder.append(" located in ").append(entry.getKey().toString());
                                builder.append(".\n");
                                i++;
                                break;
                            }
                        }
                    }
                } else {
                    for (TileView t : gameMap.allTiles()) {
                        if (t.uid().equals(tile.iterator().next())) {
                            builder.append(i);
                            builder.append(". ");
                            builder.append(AnsiColor.getAnsi(t.color()));
                            builder.append(gameMap.getCoord(t).toString());
                            builder.append(AnsiColor.getDefault());
                            builder.append(".\n");
                            i += 1;
                            break;
                        }
                    }
                }
            }
        }

        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "target", string, finalI);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void chooseAction(List<ActionView> action, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        System.out.println(description);
        builder.append("Choose your action(s):\n0. Exit selection\n");
        Iterator<ActionView> actionIterator = action.iterator();
        int i = 1;
        while(actionIterator.hasNext()){
            ActionView next = actionIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(next.getName()).append(" that costs ").append(printCost(next.getCost()));
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        builder.append("\n>> You've got the following ammo:\t");
        builder.append(printCost(gameMapView.you().ammo())).append(" \n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);


        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "action", string, finalI);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void chooseWeapon(List<WeaponView> weapon, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        System.out.println(description);
        builder.append("Choose your weapons:\n0. Exit selection\n");

        Iterator<WeaponView> weaponIterator = weapon.iterator();
        int i = 1;
        while(weaponIterator.hasNext()){
            WeaponView wv = weaponIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(wv.name());
            builder.append("\n\tBuy cost: ");
            builder.append(printCost(wv.buyCost().get(AmmoColor.RED),wv.buyCost().get(AmmoColor.YELLOW),wv.buyCost().get(AmmoColor.BLUE), false));
            builder.append("\n\tReload cost: ");
            builder.append(printCost(wv.reloadCost().get(AmmoColor.RED),wv.reloadCost().get(AmmoColor.YELLOW),wv.reloadCost().get(AmmoColor.BLUE), false));
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");

        builder.append("\n>> You've got the following ammo: \t");
        builder.append(printCost(gameMapView.you().ammo())).append(" \n");

        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "weapon", string, finalI);
        commandParser.bind(consumer);
    }

    @Override
    public void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        System.out.println(description);
        builder.append("Choose your PowerUp(s):\n0. Exit selection\n");
        Iterator<PowerUpView> puIterator = powerUp.iterator();
        int i = 1;
        while(puIterator.hasNext()){
            PowerUpView pu = puIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(AnsiColor.getAnsi(pu.ammo().toColor()));
            builder.append(pu.type().toString());
            builder.append(AnsiColor.getDefault());
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Cancel last selection\n100. Restart Selection\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "power up", string, finalI);

        commandParser.bind(consumer);
    }

    @Override
    public void chooseString(List<String> string, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        System.out.println(description);
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

        int finalI = i;
        Consumer<String> consumer =
                stringa -> choicer(optional, single, choiceId, "string", stringa, finalI);

        commandParser.bind(consumer);
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void onRollback() {
        //does nothing
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
    public void updateMap(GameMapView gameMapView, boolean forced) {
        climap = new CLIMap(gameMapView);
        if(yourPlayerChar == null) {
            yourPlayerChar = "You're the player " + AnsiColor.getAnsi(climap.getMp().you().color()) + climap.getPlayers().get(climap.getMp().you());
            System.out.println(yourPlayerChar);
            System.out.println(AnsiColor.getDefault());
        }
        if(forced || (!forced && !areEquals(this.gameMapView, gameMapView)))
            climap.printMap();
        this.gameMapView = gameMapView;
    }

    private boolean areEquals(GameMapView map1, GameMapView map2) {
        if(map1 == null || map2 == null)    return false;
        for(Coord coord : map1.allCoord()) {
            TileView tile1 = map1.getPosition(coord);
            TileView tile2 = map2.getPosition(coord);
            if(
                    !tile1.players().stream().map(ActorView::name).collect(Collectors.toList())
                    .containsAll(tile2.players().stream().map(ActorView::name).collect(Collectors.toList()))
                            ||
                            !(tile1.ammoCard().numOfBlue() == tile2.ammoCard().numOfBlue() &&
                                    tile1.ammoCard().numOfRed() == tile2.ammoCard().numOfRed() &&
                                    tile1.ammoCard().numOfYellow() == tile2.ammoCard().numOfYellow() &&
                                    tile1.ammoCard().numOfPowerUp() == tile2.ammoCard().numOfPowerUp())
            )
                return false;
        }
        return true;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Player ").append(player.getUsername()).append(" just logged ");
        if(connected){
            builder.append("in");
        } else {
            builder.append("out");
        }
        builder.append("! There are now ").append(numOfPlayer).append(" in the game");
        System.out.println(builder.toString());
    }

    @Override
    public void onStarting(String map, GameMode gameMode) {
        StringBuilder builder = new StringBuilder();
        builder.append("The game is goin' to start in a moment...\t");
        switch (gameMode) {
            case NORMAL:
                builder.append("Normal mode");
                break;
            case DOMINATION:
                builder.append("Domination mode");
                break;
            case TERMINATOR:
                builder.append("Terminator mode");
                break;
            case TURRET:
                builder.append("Turret mode");
        }
        System.out.println(builder.toString());
        if(!scanThread.isAlive())
            scanThread.start();
    }

    @Override
    public void onTimer(int timeToCount) {
        System.out.println("Game will start in " + timeToCount/1000 + " seconds");
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
        System.out.println("\n\nAdrenaline™ is a game by Group9\nLorenzo Ruffati\nCarmelo Sarta\nPietro Tenani");
        endGame();
    }

    /**
     * It shows the room which the tile belongs. If there are weapons or ammoTile and finally what players are in this
     * tile.
     * @param t to get the info from.
     */
    private void tileInfo(TileView t){
        StringBuilder builder = new StringBuilder();
        builder.append("\n>> The tile belongs to the ");
        builder.append(AnsiColor.getAnsi(t.color())).append(AnsiColor.getColorName(t.color())).append(AnsiColor.getDefault()).append(" room\n");
        if(t.spawnPoint()){
            builder.append(">> There is a spawn point for weapons in the tile.\n\n");
            if(t.weapons() != null) {
                builder.append(">> You can pick up:\n");
                for (WeaponView w : t.weapons()) {
                    builder.append("+ ").append(w.name()).append(" that costs ").append(printCost(w.buyCost())).append("\n");
                }
            }
        } else {
            if(t.ammoCard() != null) {
                builder.append(">> There is a spawn point for the following ammunition in the tile:\t");
                builder.append(printCost(t.ammoCard().numOfRed(),t.ammoCard().numOfYellow(),t.ammoCard().numOfBlue(), false)).append("\n");
            }
        }
        int i = 0;
        if(t.players().isEmpty()){
            builder.append(">> There are no players in the tile.\n");
        } else {
            builder.append(">> The following players are in the tile: \n");
            for(ActorView a: t.players()){
                builder.append(i).append(". ").append(AnsiColor.getAnsi(a.color())).append(a.name()).append("\u001B[0m \n");
                i++;
            }
        }
        System.out.println(builder.toString());
    }


    private String printCost(Map<AmmoColor, Integer> map) {
        return printCost(map.get(AmmoColor.RED), map.get(AmmoColor.YELLOW), map.get(AmmoColor.BLUE), true);
    }

    private String printCost(int red, int yellow, int blue, boolean parenthesis) {
        if(red+yellow+blue == 0) return "";
        StringBuilder out = new StringBuilder();
        out.append(AnsiColor.getAnsi(Color.red));
        for(int i = 0; i<red; i++){
            out.append("■ ");
        }
        out.append(AnsiColor.getAnsi(Color.yellow));
        for(int i = 0; i<yellow; i++){
            out.append("■ ");
        }

        out.append(AnsiColor.getAnsi(Color.blue));
        for(int i = 0; i<blue; i++){
            out.append("■ ");
        }
        //todo: change
        //fixme: change
        /*
        if(parenthesis && out.length() != 0){
            out.insert(1,'(');
            out.insert(out.length()-1,')');
        }
        */
        out.append(" ").append(AnsiColor.getDefault()).append(" ");
        return out.toString();
    }

    private String printListOfColor(List<ActorView> actorViews) {
        StringBuilder builder = new StringBuilder();
        for(ActorView actorView : actorViews) {
            //todo: should be changed
            if(actorView == null && climap.getMp().gameMode().equals(GameMode.DOMINATION))
                builder.append(AnsiColor.getAnsi(climap.getMp().dominationPointActor().values().iterator().next().color()));
            else
                builder.append( AnsiColor.getAnsi(actorView.color()) );
            builder.append("█ ");
            builder.append(AnsiColor.getDefault());
        }
        return builder.toString();
    }

    private void playerInfo(ActorView player) {
        System.out.println("\n>> The player " + AnsiColor.getAnsi(player.color()) + player.name() + "\u001B[0m" + " still got " +
                (player.getHP()-player.damageTaken().size()) + "HP left.");
        if(player.getHP()-player.damageTaken().size() < 10 ) {
            System.out.print("\n>> Damage taken :\t");
            System.out.println(printListOfColor(player.damageTaken()));
        }
        if(player.marks().size() != 0) {
            System.out.print("\n>> Marks taken:\t");
            List<ActorView> marks = new ArrayList<>();
            for(Map.Entry entry : player.marks().entrySet())
                for(int i = 0; i<(Integer)entry.getValue(); i++)
                    marks.add((ActorView)entry.getKey());
            System.out.println(printListOfColor(marks));
        }

        try {
            System.out.println("\n>> He's located in the " + climap.getMp().getCoord(player.position()).toString() + " position.");
        }
        catch (InvalidParameterException e) {
            //skip
        }
        System.out.print("\n>> He's got the following ammo:\t");
        System.out.println(printCost(player.ammo()));

        int i = 0;
        if(!player.loadedWeapon().isEmpty()) {
            System.out.println("\n>> He's got the following loaded weapons: ");
            for (WeaponView w : player.loadedWeapon()) {
                System.out.println(i + ". " + w.name());
                i++;
            }
        }
        if(!player.unloadedWeapon().isEmpty()) {
            System.out.println("\n>> He's got the following unloaded weapons: ");
            for (WeaponView w : player.unloadedWeapon()) {
                System.out.println(i + ". " + w.name());
                i++;
            }
        }
        i=0;
        if(!player.powerUp().isEmpty()) {
            System.out.println("\n>> He's got the following powerUps: ");
            for (PowerUpView w : player.powerUp()) {
                System.out.println(i + ". " + AnsiColor.getAnsi(w.ammo().toColor()) + w.type() + AnsiColor.getDefault());
                i++;
            }
        }

        System.out.println("\n\n\n");
    }

    void askInfo() {
        clearScreen();
        getPrintedMap();
        printScoreboard();
        String out;
        out = (">> 0. Exit\n") + (">> 1. Players\n") + (">> 2. Tiles\n") + (">> 3. Weapons\n");
        System.out.println(out);
    }

    private void printScoreboard() {
        StringBuilder builder = new StringBuilder();
        builder.append("Killtrack : ");
        printListOfColor(climap.getMp().skullBox().stream().map(x -> (ActorView)x.keySet()).collect(Collectors.toList()));
        System.out.println(builder.toString());
        builder = new StringBuilder();
        if(climap.getMp().gameMode().equals(GameMode.DOMINATION)) {
            builder.append("Colortrack");
            for(Map.Entry<Color, List<ActorView>> entry : climap.getMp().spawnTracker().entrySet()) {
                builder.append("\n\t");
                builder.append(AnsiColor.getColorName(entry.getKey()));
                if(entry.getKey().equals(Color.red))    builder.append("\t");
                builder.append("\t :\t");
                builder.append(printListOfColor(entry.getValue()));
            }
            builder.append("\n");
            System.out.println(builder.toString());
        }
    }

    void askPlayer() {
        int i = 0;
        for(ActorView actor : gameMapView.players()) {
            System.out.println(i + ". " + actor.name());
            i++;
        }
    }

    void choosePlayer(String str) {
        int value = 100000;
        if(str.matches(" ?\\d+ ?")) {
            value = Integer.parseInt(str);
        }
        int i = 0;
        for(ActorView actorView : gameMapView.players()) {
            if(str.equals(actorView.name())||(i == value)) {
                playerInfo(actorView);
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
        if(!str.matches(" ?\\d+ ?"))
            return;
        int value = Integer.parseInt(str);
        int i = 0;
        for(TileView tile : climap.getMp().allTiles()) {
            if(i == value) {
                tileInfo(tile);
            }
            i++;
        }
    }

    void askWeapon() {
        System.out.println("Insert the name of the weapon");
    }

    void chooseWeapon(String weaponName) {
        List<WeaponView> weapons = listOfWeapon.stream().filter(x -> x.name().equalsIgnoreCase(weaponName)).collect(Collectors.toList());
        if(weapons.size() != 1) return;
        WeaponView weapon = weapons.get(0);
        StringBuilder builder = new StringBuilder();
        builder.append(weapon.name()).append("\n");
        builder.append("Buy cost:\t").append(printCost(weapon.buyCost())).append("\n");
        builder.append("Reload cost:\t").append(printCost(weapon.reloadCost())).append("\n");
        builder.append("Effects:\n");
        for(Map.Entry entry : weapon.actionDescription().entrySet()) {
            builder.append("\t- ").append(entry.getKey()).append("\n");
            builder.append(entry.getValue()).append("\n\n");
        }
        builder.append("\n");
        System.out.println(builder.toString());
    }

    void quitGame(){
        System.out.println("Are you sure you want to quit the game? Press 'y' if you want to proceed, 'n' if you" +
                "want to go back.");
    }

    void confirmqQuit() {
        client.quit();
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