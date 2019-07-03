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

        System.out.println("Benvenuto su ADRENALINA!\n");

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
            System.out.println(">>> Inserisci il nome utente!");
            username = in.nextLine();
        }
        while(password.isEmpty()){
            System.out.println(">>> Inserisci la password!");
            password = in.nextLine();
        }
        while(color.isEmpty()){
            System.out.println(">>> Scegli il tuo colore:\n -> Grigio\n -> Rosa\n -> Giallo\n -> Verde\n -> Blu\n -> Inserisci 'y' " +
                    "se hai già un account valido.");
            color = in.nextLine();
            if(color.equalsIgnoreCase("y")) {
                client.login(username,password);
                return;
            }
            switch (color.toLowerCase()) {
                case "grigio":
                    color = "gray";
                    break;
                case "rosa":
                    color = "pink";
                    break;
                case "giallo":
                    color = "yellow";
                    break;
                case "verde":
                    color = "green";
                    break;
                case "blu":
                    color = "blue";
                    break;

                    default:
                        System.out.println("Colore non valido. Scegline uno tra i seguenti");
                        color = "";
                        break;
            }
        }
        client.login(username,password,color);
    }


    /**
     * After the player joined the game, he await for the game to start. This can happen only when there are three or
     * more players in the same game and the firstPlayer decides do start or the timer elapses.
     */
    private void waitForStart() {
        //notihng
    }


    private void pick(String choiceId) {
        pickStringMessage = "";

        List<Integer> toReturn = new ArrayList<>(chosenList);
        chosenList.clear();

        StringBuilder builder = new StringBuilder();
        builder.append("Scelte fatte:\t[ ");
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
                    System.out.println("Devi selezionare almeno un elemento " + type + ".");
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
                    System.out.println("L'indice massimo consentito è " + max+1 + "\n");
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
        builder.append("Scegli il bersaglio:\n0. Conferma selezione\n");

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
                builder.append("Opzione ");
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
                                builder.append(" il cui carattere nella mappa è '");
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
                                builder.append(" posizionato in ").append(entry.getKey().toString());
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

        builder.append("99. Rimuovi ultima scelta\n100. Ricomincia l'azione\n200. Rollback\n");
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
        builder.append("Scegli il bersaglio:\n0. Conferma selezione\n");
        Iterator<ActionView> actionIterator = action.iterator();
        int i = 1;
        while(actionIterator.hasNext()){
            ActionView next = actionIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(next.getName());
            if(next.getCost().values().stream().filter(x -> x>0).count() > 0)
                builder.append(" che costa ").append(printCost(next.getCost()));
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Rimuovi ultima scelta\n100. Ricomincia l'azione\n200. Rollback\n");
        builder.append("\n>> Possiedi queste munizioni:\t");
        builder.append(printCost(gameMapView.you().ammo())).append(" \n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);


        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "azione", string, finalI);

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
        builder.append("Scegli l'arma:\n0. Conferma selezione\n");

        Iterator<WeaponView> weaponIterator = weapon.iterator();
        int i = 1;
        while(weaponIterator.hasNext()){
            WeaponView wv = weaponIterator.next();
            builder.append(i);
            builder.append(". ");
            builder.append(wv.name());
            builder.append("\n\tPrezzo di raccolta: ");
            builder.append(printCost(wv.buyCost().get(AmmoColor.RED),wv.buyCost().get(AmmoColor.YELLOW),wv.buyCost().get(AmmoColor.BLUE), false));
            builder.append("\n\tPrezzo di ricarica: ");
            builder.append(printCost(wv.reloadCost().get(AmmoColor.RED),wv.reloadCost().get(AmmoColor.YELLOW),wv.reloadCost().get(AmmoColor.BLUE), false));
            builder.append("\n");
            i+=1;
        }
        builder.append("99. Rimuovi ultima scelta\n100. Ricomincia l'azione\n200. Rollback\n");

        builder.append("\n>> Possiedi queste munizioni:\t");
        builder.append(printCost(gameMapView.you().ammo())).append(" \n");

        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        int finalI = i;
        Consumer<String> consumer =
                string -> choicer(optional, single, choiceId, "arma", string, finalI);
        commandParser.bind(consumer);
    }

    @Override
    public void choosePowerUp(List<PowerUpView> powerUp, boolean single, boolean optional, String description, String choiceId) {
        chosenList.clear();
        StringBuilder builder = new StringBuilder();
        System.out.println(description);
        builder.append("Scegli il powerUp:\n0. Conferma selezione\n");
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
        builder.append("99. Rimuovi ultima scelta\n100. Ricomincia l'azione\n200. Rollback\n");
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
        builder.append("Scegli un'opzione:\n0. Conferma selezione\n");
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
        builder.append("99. Rimuovi ultima scelta\n100. Ricomincia l'azione\n200. Rollback\n");
        pickStringMessage = builder.toString();
        System.out.println(pickStringMessage);

        int finalI = i;
        Consumer<String> consumer =
                stringa -> choicer(optional, single, choiceId, "stringa", stringa, finalI);

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

    @Override
    public void onLostTurn(Player player) {
        pickStringMessage = "";
        System.out.println(player.getUsername() + " ha perso il turno!");
    }

    /**
     * See documentation in the View interface.
     */
    @Override
    public void terminated() { }

    /**
     * See documentation in the View interface.
     */
    @Override
    public synchronized void updateMap(GameMapView gameMapView, boolean forced) {
        climap = new CLIMap(gameMapView);
        if(yourPlayerChar == null) {
            yourPlayerChar = "Sei il giocatore " + AnsiColor.getAnsi(climap.getMp().you().color()) + climap.getPlayers().get(climap.getMp().you()) + " " + AnsiColor.getDefault();
        }
        if(forced || (!forced && !areEquals(this.gameMapView, gameMapView))) {
            System.out.println(yourPlayerChar);
            climap.printMap();
        }
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
            System.out.println("Login completato correttamente.");
        } else {
            if(invalidColor) {
                System.out.println("Questo colore non è valido, prova a sceglierne un altro\n" +
                        "se lo avevi tu, effettua una riconnessione");
            } else {
                if(invalidUsername){
                    System.out.println("Username/password non validi");
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
        builder.append("Il giocatore ").append(player.getUsername()).append(" è appena ");
        if(connected){
            builder.append("entrato");
        } else {
            builder.append("uscito");
        }
        builder.append("! Al momento ci sono ").append(numOfPlayer).append(" giocatori");
        System.out.println(builder.toString());
    }

    @Override
    public void onStarting(String map, GameMode gameMode) {
        StringBuilder builder = new StringBuilder();
        builder.append("La partita sta per iniziare...\t");
        switch (gameMode) {
            case NORMAL:
                builder.append("modalità Deathmach");
                break;
            case DOMINATION:
                builder.append("modalità Dominazione");
                break;
            case TERMINATOR:
                builder.append("modalità Terminator");
                break;
            case TURRET:
                builder.append("modalità Torrette");
        }
        System.out.println(builder.toString());
        if(!scanThread.isAlive())
            scanThread.start();
    }

    @Override
    public void onTimer(int timeToCount) {
        System.out.println("Aspettiamo per un po' qualche altro giocatore");
        System.out.println("La partita comincerà tra " + timeToCount/1000 + " secondi!");
        waitForStart();
    }

    @Override
    public void onRespawn() {
        System.out.println("Stai respawnando...");
    }

    @Override
    public void onTakeback() {
        System.out.println("Puoi usare una granata!");
    }

    @Override
    public void onTerminator() {
        System.out.println("Puoi muovere il Terminator!");
    }

    @Override
    public void onWinner(String winner, int winnerPoints, int yourPoints) {
        System.out.println(String.format("La partita è finita!%nIl vincitore è...%n%s%n Ha vinto totalizzando %d punti, tu hai ottenuto %d punti", winner, winnerPoints, yourPoints));
    }

    @Override
    public void onCredits() {
        System.out.println("\n\nAdrenaline™ is a game by Group9\nLorenzo Ruffati\nCarmelo Sarta\nPietro Tenani\n\n\n");
        endGame();
    }

    /**
     * It shows the room which the tile belongs. If there are weapons or ammoTile and finally what players are in this
     * tile.
     * @param t to get the info from.
     */
    private void tileInfo(TileView t){
        StringBuilder builder = new StringBuilder();
        builder.append("\n>> La cella appartiene alla stanza color ");
        builder.append(AnsiColor.getAnsi(t.color())).append(AnsiColor.getColorName(t.color())).append(AnsiColor.getDefault()).append(" \n");
        if(t.spawnPoint()){
            builder.append(">> In questa cella c'è uno spawn point\n\n");
            if(t.weapons() != null) {
                builder.append(">> Puoi racogliere:\n");
                for (WeaponView w : t.weapons()) {
                    builder.append("+ ").append(w.name()).append(" che costa ").append(printCost(w.buyCost())).append("\n");
                }
            }
        } else {
            if(t.ammoCard() != null) {
                builder.append(">> In questa cella ci sono queste munizioni:\t");
                builder.append(printCost(t.ammoCard().numOfRed(),t.ammoCard().numOfYellow(),t.ammoCard().numOfBlue(), false)).append("\n");
            }
        }
        int i = 0;
        if(t.players().isEmpty()){
            builder.append(">> In questa cella non ci sono giocatori\n");
        } else {
            builder.append(">> Questi giocatori sono nella cella: \n");
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
            if(actorView == null && climap.getMp().gameMode().equals(GameMode.DOMINATION))
                builder.append(AnsiColor.getAnsi(climap.getMp().dominationPointActor().values().iterator().next().color()));
            else
                if(actorView != null)
                    builder.append( AnsiColor.getAnsi(actorView.color()) );
            builder.append("█ ");
            builder.append(AnsiColor.getDefault());
        }
        return builder.toString();
    }

    private void playerInfo(ActorView player) {
        System.out.println("\n>> Il giocatore " + AnsiColor.getAnsi(player.color()) + player.name() + "\u001B[0m" + " ha ancora " +
                (player.getHP()-player.damageTaken().size()) + "punti vita.");

        if(player.score() >= 0) {
            System.out.println("\n>> Punteggio :\t" + player.score());
        }
        if(player.getHP()-player.damageTaken().size() < 10 ) {
            System.out.print("\n>> Danni subiti :\t");
            System.out.println(printListOfColor(player.damageTaken()));
        }
        if(player.marks().size() != 0) {
            System.out.print("\n>> Marchi subiti:\t");
            List<ActorView> marks = new ArrayList<>();
            for(Map.Entry entry : player.marks().entrySet())
                for(int i = 0; i<(Integer)entry.getValue(); i++)
                    marks.add((ActorView)entry.getKey());
            System.out.println(printListOfColor(marks));
        }

        try {
            System.out.println("\n>> È posizionato nella cella " + climap.getMp().getCoord(player.position()).toString());
        }
        catch (InvalidParameterException e) {
            //skip
        }
        System.out.print("\n>> Ha le seguenti munizioni:\t");
        System.out.println(printCost(player.ammo()));

        int i = 0;
        if(!player.loadedWeapon().isEmpty()) {
            System.out.println("\n>> Ha queste armi cariche: ");
            for (WeaponView w : player.loadedWeapon()) {
                System.out.println(i + ". " + w.name());
                i++;
            }
        }
        if(!player.unloadedWeapon().isEmpty()) {
            System.out.println("\n>> Ha queste armi scariche: ");
            for (WeaponView w : player.unloadedWeapon()) {
                System.out.println(i + ". " + w.name());
                i++;
            }
        }
        i=0;
        if(!player.powerUp().isEmpty()) {
            System.out.println("\n>> Ha questi powerUp: ");
            for (PowerUpView w : player.powerUp()) {
                System.out.println(i + ". " + AnsiColor.getAnsi(w.ammo().toColor()) + w.type() + AnsiColor.getDefault());
                i++;
            }
        }

        System.out.println("\n\n\n");
    }

    void askInfo() {
        clearScreen();
        System.out.println(yourPlayerChar);
        getPrintedMap();
        printScoreboard();
        String out;
        out = (">> 0. Annulla\n") + (">> 1. Giocatori\n") + (">> 2. Celle\n") + (">> 3. Armi\n");
        System.out.println(out);
    }

    private void printScoreboard() {
        StringBuilder builder = new StringBuilder();
        builder.append("Killtrack : ");
        printListOfColor(climap.getMp().skullBox().stream().map(x -> x.keySet().iterator().next()).collect(Collectors.toList()));
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
            System.out.println(i + "." + AnsiColor.getAnsi(actor.color()) + " "+actor.name() +" "+ AnsiColor.getDefault());
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
        System.out.println("Inserisci il nome dell'arma");
    }

    void chooseWeapon(String weaponName) {
        List<WeaponView> weapons = listOfWeapon.stream().filter(x -> x.name().equalsIgnoreCase(weaponName)).collect(Collectors.toList());
        if(weapons.size() != 1) return;
        WeaponView weapon = weapons.get(0);
        StringBuilder builder = new StringBuilder();
        builder.append(weapon.name()).append("\n");
        builder.append("Costo di raccolta:\t").append(printCost(weapon.buyCost())).append("\n");
        builder.append("Costo di ricarica:\t").append(printCost(weapon.reloadCost())).append("\n");
        builder.append("Effetti:\n");
        for(Map.Entry entry : weapon.actionDescription().entrySet()) {
            builder.append("\t- ").append(entry.getKey()).append("\n");
            builder.append(entry.getValue()).append("\n\n");
        }
        builder.append("\n");
        System.out.println(builder.toString());
    }

    void quitGame(){
        System.out.println("Sei sicuro di voler abbandonare la partita? (y/n)");
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