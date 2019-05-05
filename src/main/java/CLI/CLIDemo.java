package CLI;

import grabbables.Weapon;
import player.Actor;
import uid.TileUID;

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


    /**
     * Leave an ASCII character on the player position.
     * @param w the weapon to be dropped.
     * @param player that drops the weapon. (Needed also to get the position where to drop the weapon.
     */
    public void dropWeapon(Actor player, Weapon w){
        //TODO I need to get the coordinates from a TileUID.
        //toPrintMap.writeOnMap('w',player.getPawn().getMap().);
    }

    /**
     * This method is needed when there are no more weapons on the spawn point.
     * @param tile is the weapon spawn point that could be empty.
     */
    public void deleteWeapon(TileUID tile){

    }

}
