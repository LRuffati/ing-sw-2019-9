package CLI;

import grabbables.Weapon;
import player.Actor;
import java.io.FileNotFoundException;
import java.util.Objects;

public class CLIDemo {
    private static CLIMap toPrintMap;
    /**
     * To be called when the server starts the game. It generates the map (with everything included on it).
     */
    public static void start() {

        try {
            toPrintMap = new CLIMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(toPrintMap).printMap();
    }

    public CLIDemo(){
        start();
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
}
