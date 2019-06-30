package viewclasses;

import board.Coord;
import controller.GameMode;
import player.Actor;

import java.awt.*;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the GameMap that is used by the view and transmitted from the server to the client
 */
public class GameMapView implements Serializable{
    private Map<Coord, TileView> tiles = new HashMap<>();;
    private Coord maxPos;
    private ActorView you;
    private List<ActorView> players;
    private List<Map<ActorView, Integer>> skullBox;
    private Map<Color, List<ActorView>> spawnTracker;
    private Map<Coord, ActorView> dominationPointActor = new HashMap<>();
    private GameMode gameMode;


    public GameMapView(){

    }

    public GameMapView(GameMapView toCopy){
        this.tiles = toCopy.getTiles();
        this.maxPos = toCopy.maxPos();
        this.you = toCopy.you();
        this.players = toCopy.players();
        this.dominationPointActor = toCopy.dominationPointActor();
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

    public Map<Color, List<ActorView>> spawnTracker() {
        return spawnTracker;
    }

    public GameMode gameMode() {
        return gameMode;
    }

    public Map<Coord, ActorView> dominationPointActor() {
        return dominationPointActor;
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

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setDominationPointActor(Map<Coord, Actor> dominationPointActor) {
        for(Map.Entry<Coord, Actor> entry : dominationPointActor.entrySet()) {
            //todo: check pov null
            ActorView actorView = entry.getValue().pawn().generateView(this, entry.getValue().pawnID(), null);
            this.dominationPointActor.put(entry.getKey(), actorView);
        }
    }

    public void setSpawnTracker(Map<Color, List<Actor>> spawnTracker) {
        this.spawnTracker = new HashMap<>();
        for(Map.Entry<Color, List<Actor>> entry : spawnTracker.entrySet()) {
            List<ActorView> toPut = new ArrayList<>();
            for(Actor actor : entry.getValue()) {
                toPut.add(players.stream().filter(x -> x.uid().equals(actor.pawnID())).collect(Collectors.toList()).get(0));
            }
            this.spawnTracker.put(entry.getKey(), toPut);
        }
    }

    public void setSkullBox(List<Map<Actor, Integer>> skullBox){
        this.skullBox = new ArrayList<>();
        for(Map<Actor, Integer> actorIntegerMap : skullBox){
            for(Map.Entry entry : actorIntegerMap.entrySet()) {
                Actor actor = (Actor)entry.getKey();
                Integer n = (Integer)entry.getValue();
                this.skullBox.add(Map.of(players.stream().filter(x -> x.uid().equals(actor.pawnID())).collect(Collectors.toList()).get(0), n));
            }
        }
    }
}
