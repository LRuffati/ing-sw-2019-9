package view.cli;

import board.Coord;
import board.Direction;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class CLIMap {
    private static final int DIM_TILE = 5;
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
        this.maxX = gmv.maxPos().getX()* DIM_TILE;
        this.maxY = gmv.maxPos().getY()* DIM_TILE;
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
            int x = mp.getCoord(t).getY()* DIM_TILE;
            int y = mp.getCoord(t).getX()* DIM_TILE;

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
            tiles[x+1][y+4] = '═';
            tiles[x+2][y+4] = '═';
            tiles[x+3][y+4] = '═';
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
                Coord cord = new Coord(r/ DIM_TILE, c/ DIM_TILE);
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
        System.out.println("\n");
    }


    /**
     * Move a player.
     * @param a is the player to be moved
     * @param cord is the tile where the player is to be moved.
     */
    //TODO To be carefully tested, probably won't work as expected due to the searcCharacter method.
    public void moveActor(ActorView a, Coord cord){
        /*
        if(searchCharacter(players.get(mp.you())).getX()!=null && searchCharacter(players.get(mp.you())).getY()!=null)
            tiles[searchCharacter(players.get(mp.you())).getX()][searchCharacter(players.get(mp.you())).getY()]= ' ';
         */
        tiles[cord.getY()* DIM_TILE + playerPos.get(players.get(a)).getY()][cord.getX()*
                DIM_TILE + playerPos.get(players.get(a)).getX()] = players.get(a);
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
        int i = 2;
        int j = 1;
        for (Map.Entry<ActorView, Character> entry : players.entrySet()) {
            playerPos.put(entry.getValue(), new Coord(i, j));
            if(i%2==0){
                i+=1;
            } else {
                i-=1;
                j+=1;
            }
        }
    }

    /**
     * Spawn the players on the map for the first time in the game.
     */
    private void spawnPlayers(){
        for(TileView t : mp.allTiles()){
            for(ActorView a: t.players()){
                tiles[mp.getCoord(t).getY()* DIM_TILE +playerPos.get(players.get(a)).getX()]
                        [mp.getCoord(t).getX()* DIM_TILE +playerPos.get(players.get(a)).getY()]
                        = players.get(a);
            }
        }
    }

    /**
     * It shows a version of the Map on the CLI where only the selectable targets are colored.
     * @param targetViewList is the list of the selectable targets.
     */
    public void applyTarget(List<TargetView> targetViewList){
        GameMapView targetMap = new GameMapView(mp);
        List<TileUID> tiless;
        List<DamageableUID> actors;
        Map<TileView, Color> tilesColor = new HashMap<>();
        Map<ActorView, Color> actorsColor = new HashMap<>();
        for(TargetView target :  targetViewList) {
            tiless = target.getTileUIDList();
            actors = target.getDamageableUIDList();
            if(!tiless.isEmpty()) {
                for (TileView t : targetMap.allTiles()) {
                    if (!tiless.contains(t.uid())) {
                        tilesColor.put(t, t.color());
                        t.setColor(Color.DARK_GRAY);
                    }
                }
            }
            if(!actors.isEmpty()) {
                for (ActorView a : targetMap.players()) {
                    if (!actors.contains(a.uid())) {
                        actorsColor.put(a, a.color());
                        a.setColor(Color.lightGray);
                    }
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

    public Map<ActorView, Character> getPlayers() {
        return players;
    }

    public GameMapView getMp() {
        return mp;
    }

    public Map<Character, Coord> getPlayerPos() {
        return playerPos;
    }
}