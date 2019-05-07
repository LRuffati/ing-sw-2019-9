package CLI;

import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.Weapon;
import player.Actor;
import uid.TileUID;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CLIMap {
    private int maxY;
    private int maxX;
    private Character[][] tiles;
    private Map<Actor,Character> players;
    private GameMap mp;

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

        this.mp = builder.getMap();
        this.maxX = builder.getMap().getMaxPos().getX()*3;
        this.maxY = builder.getMap().getMaxPos().getY()*3;
        tiles = new Character[maxX][maxY];
        players = new HashMap<>();
        //TODO Is there a cleaner way to put the ASCII characters for the players? I hope so.
        String dictionary = "abcdefghi";
        int i = 0;
        for(Actor a:builder.getActorList()){
            players.put(a,dictionary.charAt(i));
            i++;
        }
        generateMap(builder.getMap());
    }

    /**
     * Fill the attribute tiles with the right ASCII characters.
     */
    private void generateMap(GameMap gm){
        for(TileUID t: gm.allTiles()){

            int x = gm.getCoord(t).getY()*3;
            int y = gm.getCoord(t).getX()*3;
            tiles[x][y] = '╔';
            tiles[x+1][y] = '═';
            tiles[x+2][y]= '╗';
            tiles[x+2][y+1] = '║';
            tiles[x][y+1] = '║';
            tiles[x][y+2] = '╚';
            tiles[x+1][y+2] = '=';
            tiles[x+2][y+2] = '╝';
        }
        for(int i = 0; i < tiles[0].length; i++){
            for(int j = 0; j< tiles.length; j++){
                if(tiles[j][i]==null) tiles[j][i] = ' ';
            }
        }
    }

    /**
     * Print on the command line the map generated with the correct ASCII characters and ANSI colors.
     */
    void printMap(){
        for (int r = 0; r < maxY; r++) {
            System.out.println();
            for (int c = 0; c < maxX; c++) {
                Coord cord = new Coord(r/3, c/3);
                if(mp.exists(cord))
                    System.out.print(mp.getRoom(mp.room(mp.getPosition(cord))).getAnsi() +  tiles[c][r] + "\u001B[0m");
            }
        }
    }

    /**
     * Put the current players in the map with the correct ASCII characters.
     * @param player is the player to be put on the map.
     * @param pos is the position where to place the ASCII character.
     */
    public void movePlayer(Actor player, Coord pos){
        tiles[pos.getX()][pos.getY()] = players.get(player);
    }

    /**
     * Put weapons and Ammotiles in the map with correct ASCII characters.
     * @param spawnWeapon
     */
    private void putWeaponsAndAmmotiles(Coord spawnWeapon){
        tiles[spawnWeapon.getX()][spawnWeapon.getY()] = 'w';
    }

    public void writeOnMap(Character ascii, Coord pos){
        tiles[pos.getX()][pos.getY()] = ascii;
    }
}
