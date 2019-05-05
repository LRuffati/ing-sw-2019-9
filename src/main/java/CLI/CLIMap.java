package CLI;

import gamemanager.GameBuilder;
import java.io.FileNotFoundException;

public class CLIMap {
    private int maxY;
    private int maxX;
    private String[][] tiles;

    //TODO Doesn't work as expected, I think the problem is a bad usage of the GameBuilder constructor.
    public CLIMap() throws FileNotFoundException {
        GameBuilder builder = new GameBuilder(
                    "src/resources/map1.txt", null, "src/resources/ammoTile.txt",
                    "src/resources/powerUp.txt", 3);

        this.maxX = builder.getMap().getMaxPos().getX();
        this.maxY = builder.getMap().getMaxPos().getY();
        tiles = new String[maxX][maxY];
        generateMap();
    }

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

    public void printMap(){
        for (int r = 0; r < maxY; r++) {
            System.out.println();
            for (int c = 0; c < maxX; c++) {
                System.out.print(tiles[r][c]);
            }
        }
    }
}
