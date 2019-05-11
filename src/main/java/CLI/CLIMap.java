package CLI;

import board.Coord;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.Weapon;
import player.Actor;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TileView;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CLIMap {
    private final int dimTile = 5;
    private int maxY;
    private int maxX;
    private Character[][] tiles;
    private Map<ActorView,Character> players;
    private GameMapView mp;
    private Map<Character,Coord> playerPos;

    /**
     * The object will generate an ASCII map to be printed on the command line.
     * @throws FileNotFoundException will probably be deleted, the map to be taken won't be to be generated
     * every time.
     */
    public CLIMap(GameMapView gmv) throws FileNotFoundException {
        this.mp = gmv;
        this.maxX = gmv.maxPos().getX()*dimTile;
        this.maxY = gmv.maxPos().getY()*dimTile;
        tiles = new Character[maxX][maxY];
        players = new HashMap<>();
        playerPos = new HashMap<>();
        //TODO Is there a cleaner way to put the ASCII characters for the players? I hope so.
        String dictionary = "abcdefghi";
        int i = 0;

        generateMap();
        for(ActorView a : gmv.otherPlayers()){
            players.put(a,dictionary.charAt(i));
            i++;
        }
        setPlayersPos();
        spawnPlayers();
    }

    /**
     * Fill the attribute tiles with the right ASCII characters.
     */
    private void generateMap(){

        for(TileView t: mp.allTiles()){
            int x = mp.getCoord(t).getY()*dimTile;
            int y = mp.getCoord(t).getX()*dimTile;

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
            if(t.spawnPoint()){
                tiles[x+1][y+1] = 's';
            }
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
                if(mp.allCoord().contains(cord))
                    //TODO add the escape "\0×1B" character in the following to print(s) to maintain the map. Won't work on Intellij.
                    System.out.print(mp.getPosition(cord).getAnsi() +  tiles[c][r] + "\u001B[0m");
                else System.out.print(" ");
            }
        }
    }

    /**
     * Put the current players in the map with the correct ASCII characters.
     * @param player is the player to be put on the map.
     * @param tile is the tile where the player is to be moved.
     */
    //TODO To be carefully tested, probably won't work as expected due to the searcCharacter method.
    public void movePlayer(Actor player, TileView tile){
        if(searchCharacter(players.get(player)).getX()!=null && searchCharacter(players.get(player)).getY()!=null)
            tiles[searchCharacter(players.get(player)).getX()][searchCharacter(players.get(player)).getY()]= ' ';
        tiles[mp.getCoord(tile).getY()*dimTile + playerPos.get(players.get(player)).getY()][mp.getCoord(tile).getX()*
                dimTile + playerPos.get(players.get(player)).getX()] = players.get(player);
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
        tiles[pos.getY()][pos.getX()] = ascii;
    }

    /**
     * Search a character in the map game and returns its coordinates.
     * @param ascii to search on the map.
     * @return the physical coordinates of the characters.
     */
    public Coord searchCharacter(Character ascii){
        for(int i = 0; i < tiles[0].length; i++){
            for(int j = 0; j< tiles.length; j++){
                //TODO: replace == with .equals ?
                if(tiles[i][j]==ascii) return new Coord(i,j);
            }
        }
        return null;
    }

    /**
     * Set the static position of every player's ascii character in a tile.
     */
    //TODO WARNING: we must be sure that the number of players are less than six.
    public void setPlayersPos(){
        int i = 1;
        int j = 0;
        for(Map.Entry<ActorView,Character> entry : players.entrySet()){
            playerPos.put(entry.getValue(),new Coord(i,j));
            i++;
            j++;
        }
    }

    /**
     * Spawn the players on the map for the first time in the game.
     */
    public void spawnPlayers(){
        for(TileView t : mp.allTiles()){
            for(ActorView a: t.players()){
                //TODO Check if EmptyTile!
                tiles[mp.getCoord(t).getY()*dimTile+playerPos.get(players.get(a)).getY()][mp.getCoord(t).getX()*dimTile
                        +playerPos.get(players.get(a)).getX()] = players.get(a);
            }
        }
    }
}
