package CLI;

import board.Coord;
import board.GameMap;
import controllerclient.ClientController;
import controllerclient.ClientControllerClientInterface;
import controllerclient.View;
import controllerresults.ControllerActionResultClient;
import grabbables.Weapon;
import network.exception.InvalidLoginException;
import network.socket.client.Client;
import player.Actor;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.*;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

public class CLIDemo implements View {
    private static CLIMap toPrintMap;
    private Scanner in = new Scanner(System.in);
    private ClientController client;
    /**
     * To be called when the server starts the game. It generates the map (with everything included on it).
     */
    public void start(GameMapView gmv) {
        toPrintMap = new CLIMap(gmv);
    }

    public CLIDemo(ClientController client){
        this.client = client;

    }

    /**
     * Method to be called from other classes. Intended to make the CLIMap class not called from other classes.
     */
    public void getPrintedMap(){
        toPrintMap.printMap();
    }

    /**
     * Method to introduce a new player to the game and show the initial options.
     * It initially clear the whole command line, then it shows the title of the game.
     */
    public void greetings(){
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

        System.out.println("Type '1' to join a game.");
        boolean joining = false;
        if(in.nextLine().equals("1")) joining = joinGame();
        if(joining) System.out.println("Welcome to ADRENALINE!\nPlease, wait for other players to join.");

    }

    public boolean joinGame(){
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
            System.out.println(">>> Choose your color:\n -> Gray\n -> Purple\n -> Yellow\n -> Green\n -> Blue");
            color = in.nextLine();
            //TODO gestisci colori
        }

        try {
            return client.login(username,password,color);
        } catch (RemoteException | InvalidLoginException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Print the event "Player user uses powerUp pu" on the cli.
     */
    public void addPowerUpNotYou(ActorView user, PowerUpView pu){
        System.out.println(user.getAnsi() + "Player " + user.name() + " is using " + pu.type().toString());
    }

    public void addPowerUpYou(ActorView user, PowerUpView pu){
        System.out.println(user.getAnsi() + "You are using " + pu.type().toString());
    }

    /**
     * Show who attacked who.
     */
    //TODO Check if passing a Map object is the better way.
    public void addDmg(ActorView attacker, Map<ActorView,Integer> actorDamaged){
        for(Map.Entry<ActorView,Integer> entry: actorDamaged.entrySet())
            System.out.println(attacker.getAnsi() + "Player " + attacker.name() + " shot ⌐╦╦═─ player " + entry.getKey()
                    .name() + " for a damage of " + entry.getValue().toString() + ".");
    }

    public void addMark(ActorView marker, Map<ActorView,Integer> marked){
        for(Map.Entry<ActorView,Integer> entry: marked.entrySet())
            System.out.println(marker.getAnsi() + "Player " + marker.name() + " marked ❌ player " + entry.getKey()
                    .name() + " for a number of marks of " + entry.getValue().toString() + ".");
    }

    public void disconnectedPlayer(ActorView a){
        System.out.println(a.getAnsi() + "Player " + a.name() + " has disconnected =║= from the game. Abuse him!");
    }

    public void reconnectedPlayer(ActorView a){
        System.out.println(a.getAnsi() + "Player " + a.name() + " has reconnected === to the game. Run!");
    }

    public void displayTimeLeft(Duration timeLeft){
        System.out.println(timeLeft.toString() + " is the time left.");
    }

    public void displayScoreAll(ActorListView players){
        for(ActorView a:players.getActorList()){
            System.out.println(a.getAnsi() + "Player " + a.name() + " gained score ✺(^O^)✺ is " + a.score() + ".");
        }
    }

    public void addKill(ActorView killer, ActorView victim){
        System.out.println(killer.getAnsi() + "Player " + killer.name() + " frag \uD83D\uDC80 player " + victim.name() + ".");
    }

    public void printAppliedTarget(List<TargetView> targetViewList){
        toPrintMap.applyTarget(targetViewList);

    }

    @Override
    public void chooseTarget(GameMapView gameMap, ControllerActionResultClient elem, List<TargetView> target) {
        CLIMap map = new CLIMap(gameMap);
        map.applyTarget(target);
        System.out.println("Choose your target:\n");
        Iterator<TargetView> targetIterator = target.iterator();
        int i = 0;
        while(targetIterator.hasNext()){
            TargetView tw = targetIterator.next();
            Collection<DamageableUID> dmg = tw.getDamageableUIDList();
            Collection<TileUID> tile = tw.getTileUIDList();
            if(!dmg.isEmpty()){
                for(ActorView a: gameMap.players()){
                    if(a.uid().equals(dmg.iterator().next())){
                        System.out.println(a.getAnsi() + i + ". " + a.name());
                        i+=1;
                        break;
                    }
                }
            } else {
                for(TileView t : gameMap.allTiles()){
                    if(t.uid().equals(tile.iterator().next())){
                        System.out.println(t.getAnsi() + i + ". " + gameMap.getCoord(t).toString());
                        i+=1;
                        break;
                    }
                }
            }
        }

        boolean flag = false;

        while(!flag) {
            try {
                i = in.nextInt();
                flag = true;
            } catch (InputMismatchException e) {
                System.out.println("Please, pick a target typing ONLY his index on the line.");
            }
        }

        List<Integer> l = new ArrayList<>();
        l.add(i);
        try {
            client.pick(elem,l);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseAction(ControllerActionResultClient elem, List<ActionView> action) {
        System.out.println("Choose your action:\n");
        Iterator<ActionView> actionIterator = action.iterator();
        int i = 0;
        while(actionIterator.hasNext()){
            System.out.println(i + ". " + actionIterator.next().getName());
            i+=1;
        }

        boolean flag = false;

        while(!flag) {
            try {
                i = in.nextInt();
                flag = true;
            } catch (InputMismatchException e) {
                System.out.println("Please, pick an action typing ONLY his index on the line.");
            }
        }

        List<Integer> l = new ArrayList<>();
        l.add(i);
        try {
            client.pick(elem,l);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseWeapon(ControllerActionResultClient elem, List<WeaponView> weapon) {
        System.out.println("Choose your weapons:\n0. Exit selection");
        Iterator<WeaponView> weaponIterator = weapon.iterator();
        int i = 1;
        List<Integer> l = new ArrayList<>();
        while(weaponIterator.hasNext()){
            WeaponView wv = weaponIterator.next();
            System.out.println(i + ". " + wv.name());
            i+=1;
        }
        while(true){
            boolean flag = false;

            while(!flag) {
                try {
                    i = in.nextInt();
                    flag = true;
                } catch (InputMismatchException e) {
                    System.out.println("Please, pick a weapon typing ONLY his index on the line.");
                }
            }
            if(i!=0) l.add(i-1); else break;
        }

        try {
            client.pick(elem,l);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        System.out.println("Rollback executed.");
    }

    @Override
    public void terminated() {
        System.out.println("Action finally executed.");
    }

    @Override
    public void updateMap(GameMapView gameMapView) {
        CLIMap cm = new CLIMap(gameMapView);
        cm.printMap();
    }

    public void tileInfo(TileView t){
        System.out.print("\n>> The tile belongs to the ");
        System.out.print(t.getAnsi() + t.color().toString() + " room\n");
        if(t.spawnPoint()){
            System.out.println(">> There is a spawn point for weapons in the tile.\n");
            System.out.println(">> You can pick up: ");
            for(WeaponView w : t.weapons()){
                System.out.println(" +" + w.name());
            }
        } else {
            System.out.println(">> There is a spawn point for ammunition in the tile.\n");
        }

        if(t.players().isEmpty()){
            System.out.println(">> There are no players in the tile.");
        } else {
            System.out.println(">> The following players are in the tile: ");
            for(ActorView a: t.players()){
                System.out.println(" +" + a.getAnsi() + a.name());
            }
        }
    }
}
