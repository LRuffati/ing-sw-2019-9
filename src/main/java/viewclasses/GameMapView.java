package viewclasses;

import board.Coord;
import player.Actor;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the GameMap that is used by the view and transmitted from the server to the client
 */
public class GameMapView implements Serializable{
    private Map<Coord, TileView> tiles;
    private Coord maxPos;
    private ActorView you;
    private List<ActorView> players;
    private List<Map<ActorView, Integer>> skullBox;


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

    public List<Map<ActorView, Integer>> skullBox() {
        return skullBox;
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

    public void setSkullBox(List<Map<Actor, Integer>> skullBox){
        this.skullBox = new ArrayList<>();
        for(Map<Actor, Integer> actorIntegerMap : skullBox){
            for(Map.Entry entry : actorIntegerMap.entrySet()){
                Actor actor = (Actor)entry.getKey();
                Integer n = (Integer)entry.getValue();
                this.skullBox.add(Map.of(players.stream().filter(x -> x.uid().equals(actor.pawnID())).collect(Collectors.toList()).get(0), n));
            }
        }
    }
}
