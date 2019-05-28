package viewclasses;

import board.Coord;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class contains the GameMap that is used by the view and transmitted from the server to the client
 */
public class GameMapView implements Serializable{
    private Map<Coord, TileView> tiles;
    private Coord maxPos;
    private ActorView you;
    private List<ActorView> players;


    public GameMapView(){
        tiles = new HashMap<>();
    }

    public GameMapView(GameMapView toCopy){
        this.tiles = toCopy.getTiles();
        this.maxPos = toCopy.maxPos();
        this.you = toCopy.you();
        this.players = toCopy.players();
    }

    public Coord maxPos(){
        return maxPos;
    }

    public Collection<TileView> allTiles(){
        return tiles.values();
    }

    public Map<Coord, TileView> getTiles() {
        return tiles;
    }

    public Set<Coord> allCoord(){
        return tiles.keySet();
    }

    public Coord getCoord(TileView tile){
        for(Map.Entry entry : tiles.entrySet()){
            if(entry.getValue().equals(tile))
                return (Coord)entry.getKey();
        }
        throw new InvalidParameterException("This tile does not exists");
    }

    public TileView getPosition(Coord coord){
        for(Map.Entry entry : tiles.entrySet()){
            if(entry.getKey().equals(coord)){
                return (TileView)entry.getValue();
            }
        }
        throw new InvalidParameterException("This coord does not exists");
    }

    public List<ActorView> players() {
        return players;
    }

    public ActorView you() {
        return you;
    }


    public void setTiles(Map<Coord, TileView> tiles){
        this.tiles = tiles;
    }
    public void setTile(Coord coord, TileView tile){
        tiles.put(coord, tile);
    }

    public void setMax(Coord maxPos){
        this.maxPos = maxPos;
    }

    public void setYou(ActorView you) {
        this.you = you;
    }

    public void setPlayers(List<ActorView> players) {
        this.players = players;
    }
}
