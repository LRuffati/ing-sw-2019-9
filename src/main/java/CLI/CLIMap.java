package CLI;

import board.Coord;
import board.Direction;
import board.GameMap;
import gamemanager.GameBuilder;
import grabbables.Weapon;
import player.Actor;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

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
    public CLIMap(GameMapView gmv){
        this.mp = gmv;
        this.maxX = gmv.maxPos().getX()*dimTile;
        this.maxY = gmv.maxPos().getY()*dimTile;
        tiles = new Character[maxX][maxY];
        players = new HashMap<>();
        playerPos = new HashMap<>();
        String dictionary = "abcdefghi";
        int i = 0;

        generateMap();
        for(ActorView a : gmv.players()){
            players.put(a,dictionary.charAt(i++));
        }
        setPlayersPos();
        spawnPlayers();
    }

    /**
     * Fill the attribute tiles with the right ASCII characters:
     * First it places every wall for every tile;
     * Then it places spawn points and AmmoTiles;
     * Finally it places the doors.
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
            } else {

                tiles[x+1][y+1] = 't';
            }
            for(Map.Entry<Direction,String> entry: t.nearTiles().entrySet()){
                if(entry.getValue().equals("Door")){
                    switch (entry.getKey()){
                        case UP:
                            tiles[x+2][y] = ' ';
                            break;

                        case DOWN:
                            tiles[x+2][y+4] = ' ';
                            break;

                        case LEFT:
                            tiles[x][y+2] = ' ';
                            break;

                        case RIGHT:
                            tiles[x+4][y+2] = ' ';
                            break;
                    }
                }
                if(entry.getValue().equals("Tile")){
                    switch (entry.getKey()){
                        case UP:
                            tiles[x+1][y] = ' ';
                            tiles[x+2][y] = ' ';
                            tiles[x+3][y] = ' ';
                            break;

                        case DOWN:
                            tiles[x+1][y+4] = ' ';
                            tiles[x+2][y+4] = ' ';
                            tiles[x+3][y+4] = ' ';
                            break;

                        case LEFT:
                            tiles[x][y+1] = ' ';
                            tiles[x][y+2] = ' ';
                            tiles[x][y+3] = ' ';
                            break;

                        case RIGHT:
                            tiles[x+4][y+1] = ' ';
                            tiles[x+4][y+2] = ' ';
                            tiles[x+4][y+3] = ' ';
                            break;
                    }
                }
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
        //System.out.println(mp.allCoord().contains(new Coord(0,0)));
        for (int r = 0; r < maxY; r++) {
            System.out.println();
            for (int c = 0; c < maxX; c++) {
                Coord cord = new Coord(r/dimTile, c/dimTile);
                boolean flag = true;
                if(mp.allCoord().contains(cord)) {
                    //TODO add the escape "\0×1B" character in the following to print(s) to maintain the map. Won't work on Intellij.
                    for (Map.Entry<ActorView, Character> entry : players.entrySet()) {
                        if(tiles[c][r].equals(entry.getValue())){
                            System.out.print(entry.getKey().getAnsi() + tiles[c][r] + "\u001B[0m");
                            flag = false;
                            break;
                        }
                    }
                    if(flag) System.out.print(mp.getPosition(cord).getAnsi() + tiles[c][r] + "\u001B[0m");
                } else System.out.print(" ");
            }
        }
    }

    public void addAmmo(TileView tile){
        int x = mp.getCoord(tile).getX()*dimTile;
        int y = mp.getCoord(tile).getY()*dimTile;
        AmmoCardView ammo = tile.ammoCard();
        tiles[y][x] = Character.forDigit(ammo.numOfBlue(),10);
        tiles[y+1][x] = Character.forDigit(ammo.numOfRed(),10);
        tiles[y+2][x] = Character.forDigit(ammo.numOfYellow(),10);

        if(ammo.numOfPowerUp() != 0)
            if(ammo.numOfBlue() == 0)
                tiles[y][x] = 'P';
            if(ammo.numOfRed() == 0)
                tiles[y+1][x] = 'P';
            if(ammo.numOfYellow() == 0)
                tiles[y+2][x] = 'P';
    }


    /**
     * Put the current players in the map with the correct ASCII characters.
     * @param player is the player to be put on the map.
     * @param cord is the tile where the player is to be moved.
     */
    //TODO To be carefully tested, probably won't work as expected due to the searcCharacter method.
    public void movePlayer(ActorView player, Coord cord){
        if(searchCharacter(players.get(player)).getX()!=null && searchCharacter(players.get(player)).getY()!=null)
            tiles[searchCharacter(players.get(player)).getX()][searchCharacter(players.get(player)).getY()]= ' ';
        tiles[cord.getY()*dimTile + playerPos.get(players.get(player)).getY()][cord.getX()*
                dimTile + playerPos.get(players.get(player)).getX()] = players.get(player);
    }

    /**
     * Put weapons in the map with correct ASCII characters checking where in the map is a spawn point.
     * It places the character right below the spawn character.
     */
    //TODO To be fixed but now it's not the moment nor the place.
    private void putWeapons(){
        while(searchCharacter('s')!=null){
            tiles[searchCharacter('s').getY()+1][searchCharacter('s').getX()]='w';
        }
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
                if(tiles[j][i].equals(ascii)) return new Coord(j,i);
            }
        }
        return null;
    }

    /**
     * Set the static position of every player's ascii character in a tile.
     */
    private void setPlayersPos(){
        if(players.size()<=6) {
            int i = 1;
            int j = 0;
            for (Map.Entry<ActorView, Character> entry : players.entrySet()) {
                playerPos.put(entry.getValue(), new Coord(i, j));
                i++;
                j++;
            }
        } else System.out.println("Too many players!");
    }

    /**
     * Spawn the players on the map for the first time in the game.
     */
    private void spawnPlayers(){
        for(TileView t : mp.allTiles()){
            for(ActorView a: t.players()){
                tiles[mp.getCoord(t).getY()*dimTile+playerPos.get(players.get(a)).getY()]
                        [mp.getCoord(t).getX()*dimTile+playerPos.get(players.get(a)).getX()]
                        = players.get(a);
            }
        }
    }

    public void applyTarget(List<TargetView> targetViewList){
        GameMapView targetMap = new GameMapView(mp);
        Collection<TileUID> tiles;
        Collection<DamageableUID> actors;
        Map<TileView, Color> tilesColor = new HashMap<>();
        Map<ActorView, Color> actorsColor = new HashMap<>();
        for(TargetView target :  targetViewList) {
            tiles = target.getTileUIDList();
            actors = target.getDamageableUIDList();
            for (TileView t : targetMap.allTiles()) {
                if (!tiles.contains(t.uid())) {
                    tilesColor.put(t,t.color());
                    t.setColor(Color.DARK_GRAY);
                }
            }
            for (ActorView a : targetMap.players()) {
                if (!actors.contains(a.uid())) {
                    actorsColor.put(a,a.color());
                    a.setColor(Color.lightGray);
                }
            }
        }
        printMap();

        for(Map.Entry<TileView,Color> entry: tilesColor.entrySet()){
            entry.getKey().setColor(entry.getValue());
        }

        for(Map.Entry<ActorView,Color> entry: actorsColor.entrySet()){
            entry.getKey().setColor(entry.getValue());
        }

    }

}