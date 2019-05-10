package viewclasses;

import board.Coord;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the GameMap that is used by the view and transmitted from the server to the client
 */
public class GameMapView implements Serializable {
    private Map<Coord, TileView> tiles;
    private Coord maxPos;
    private ActorView you;
    private List<ActorView> otherPlayers;


    public GameMapView(){
        tiles = new HashMap<>();
    }

    public Coord maxPos(){
        return maxPos;
    }

    public Collection<TileView> allTiles(){
        return tiles.values();
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

    public List<ActorView> otherPlayers() {
        return otherPlayers;
    }

    public ActorView you() {
        return you;
    }


    public void setTiles(Map<Coord, TileView> tiles){
        this.tiles = tiles;
    }

    public void setMax(Coord maxPos){
        this.maxPos = maxPos;
    }

    public void setYou(ActorView you) {
        this.you = you;
    }

    public void setOtherPlayers(List<ActorView> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }
}
