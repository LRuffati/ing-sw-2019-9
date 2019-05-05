package CLI;

import gamemanager.GameBuilder;
import player.Actor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

public class CLIMap {
    private int maxY;
    private int maxX;
    private String[][] tiles;
    private Map<Actor,Character> players;


    //TODO Doesn't work as expected, I think the problem is a bad usage of the GameBuilder constructor.

    /**
     * The object will generate an ASCII map to be printed on the command line.
     * @throws FileNotFoundException will probably be deleted, the map to be taken won't be to be generated
     * every time.
     */
    public CLIMap() throws FileNotFoundException {
        GameBuilder builder = new GameBuilder(
                    "src/resources/map1.txt", null, "src/resources/ammoTile.txt",
                    "src/resources/powerUp.txt", 3);

        this.maxX = builder.getMap().getMaxPos().getX();
        this.maxY = builder.getMap().getMaxPos().getY();
        tiles = new String[maxX][maxY];
        //TODO Is there a cleaner way to put the ASCII characters for the players? I hope so.
        String dictionary = "abcdefghi";
        int i = 0;
        for(Actor a:builder.getActorList()){
            players.put(a,dictionary.charAt(i));
            i++;
        }
        generateMap();
    }

    /**
     * Fill the attribute tiles with the right ASCII characters.
     */
    private void generateMap(){
        tiles[0][0] = "╔";
        for (int c = 1; c < maxX - 1; c++) {
            tiles[0][c] = "═";
        }

        tiles[0][maxX - 1] = "╗";

        for (int r = 1; r < maxY - 1; r++) {
            tiles[r][0] = "║";
            for (int c = 1; c < maxX - 1; c++) {
                tiles[r][c] = " ";
            }
            tiles[r][maxX-1] = "║";
        }

        tiles[maxY - 1][0] = "╚";
        for (int c = 1; c < maxX - 1; c++) {
            tiles[maxY - 1][c] = "═";
        }

        tiles[maxY - 1][maxX - 1] = "╝";

    }

    /**
     * Print on the command line the map generated with the correct ASCII characters.
     */
    public void printMap(){
        for (int r = 0; r < maxY; r++) {
            System.out.println();
            for (int c = 0; c < maxX; c++) {
                System.out.print(tiles[r][c]);
            }
        }
    }

    /**
     * Put the current players in the map with the correct ASCII characters.
     */
    private void putPlayer(){

    }

    /**
     * Put weapons and Ammotiles in the map with correct ASCII characters.
     */
    private void putWeaponsAndAmmotiles(){

    }
}
