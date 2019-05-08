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
    private final int dimTile = 5;
    private int maxY;
    private int maxX;
    private Character[][] tiles;
    private Map<Actor,Character> players;
    private GameMap mp;

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
        this.maxX = builder.getMap().getMaxPos().getX()*dimTile;
        this.maxY = builder.getMap().getMaxPos().getY()*dimTile;
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
            int x = gm.getCoord(t).getY()*dimTile;
            int y = gm.getCoord(t).getX()*dimTile;

            tiles[x][y] = '╔';
            tiles[x+1][y] = '═';
            tiles[x+2][y] = '═';
            tiles[x+3][y] = '═';
            tiles[x+4][y]= '╗';
            tiles[x+4][y+1] = '║';
            tiles[x+4][y+2] = '║';
            tiles[x+4][y+3] = '║';
            tiles[x][y+1] = '║';
            tiles[x][y+2] = '║';
            tiles[x][y+3] = '║';
            tiles[x][y+4] = '╚';
            tiles[x+1][y+4] = '=';
            tiles[x+2][y+4] = '=';
            tiles[x+3][y+4] = '=';
            tiles[x+4][y+4] = '╝';
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
                Coord cord = new Coord(r/dimTile, c/dimTile);
                if(mp.exists(cord))
                    //TODO add the escape "\0×1B" character in the following to print(s) to maintain the map. Won't work on Intellij.
                    System.out.print(mp.getRoom(mp.room(mp.getPosition(cord))).getAnsi() +  tiles[c][r] + "\u001B[0m");
                else System.out.print(" ");
            }
        }
    }

    /**
     * Put the current players in the map with the correct ASCII characters.
     * @param player is the player to be put on the map.
     * @param pos is the position where to place the ASCII character.
     */
    public void movePlayer(Actor player, Coord pos){
        if(searchCharacter(players.get(player)).getX()!=null && searchCharacter(players.get(player)).getY()!=null)
            tiles[searchCharacter(players.get(player)).getX()][searchCharacter(players.get(player)).getY()]= ' ';
        tiles[pos.getX()*dimTile][pos.getY()*dimTile] = players.get(player);
    }

    /**
     * Put weapons and Ammotiles in the map with correct ASCII characters.
     * @param spawnWeapon
     */
    private void putWeaponsAndAmmotiles(Coord spawnWeapon){
        Character[][] tile = new Character[dimTile][dimTile];
        //tile[dimTile][dimTile/2] =
        tiles[spawnWeapon.getX()][spawnWeapon.getY()] = 'w';
    }

    /**
     *
     * @param ascii the character to write.
     * @param pos where to write the character.
     */
    public void writeOnMap(Character ascii, Coord pos){
        tiles[pos.getX()][pos.getY()] = ascii;
    }

    /**
     * Search a character in the map game and returns its coordinates.
     * @param ascii to search on the map.
     * @return the physical coordinates of the characters.
     */
    public Coord searchCharacter(Character ascii){
        for(int i = 0; i < tiles[0].length; i++){
            for(int j = 0; j< tiles.length; j++){
                if(tiles[i][j]==ascii) return new Coord(i,j);
            }
        }
        return null;
    }
}
