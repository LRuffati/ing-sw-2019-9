package CLI;

import grabbables.Weapon;
import player.Actor;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.PowerUpView;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CLIDemo {
    private static CLIMap toPrintMap;
    /**
     * To be called when the server starts the game. It generates the map (with everything included on it).
     */
    public static void start(GameMapView gmv) {
        try {
            toPrintMap = new CLIMap(gmv);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(toPrintMap).printMap();
    }

    public CLIDemo(GameMapView gmv){
        start(gmv);
    }

    /**
     * Leave an ASCII character on the player position.
     * @param w the weapon to be dropped.
     * @param player that drops the weapon. (Needed also to get the position where to drop the weapon.
     */
    public void dropWeapon(Actor player, Weapon w){
        toPrintMap.writeOnMap('w',player.getPawn().getMap().getCoord(player.getPawn().getTile()));
    }

    /**
     * This method is needed when there are no more weapons on the spawn point.
     * @param player is the player on the spawn point.
     */
    public void deleteWeapon(Actor player){
        if(player.getGm().getTile(player.getPawn().getTile()).spawnPoint()){
            toPrintMap.writeOnMap('/', player.getPawn().getMap().getCoord(player.getPawn().getTile()));
        }
    }

    /**
     * Method to be called from other classes. Intended to make the CLIMap class not called from other classes.
     */
    public void getPrintedMap(){
        toPrintMap.printMap();
    }

    /**
     * Method to introduce a new player to the game and show the initial options.
     */
    public void greetings(){
        System.out.println("Hello there, shooter. State your name: \n");
        System.console().readLine();
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

    public void displayTimeLeft(){

    }

    public void displayScoreAll(List<ActorView> players){
        for(ActorView a:players){
            System.out.println(a.getAnsi() + "Player " + a.name() + " gained score ✺(^O^)✺ is " + a.score() + ".");
        }
    }

    public void addKill(ActorView killer, ActorView victim){
        System.out.println(killer.getAnsi() + "Player " + killer.name() + " frag \uD83D\uDC80 player " + victim.name() + ".");
    }

}
