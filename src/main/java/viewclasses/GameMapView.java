package viewclasses;

import board.Coord;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class GameMapView implements Serializable {
    private Map<Coord, TileView> tiles;
    private Coord maxPos;

    public GameMapView(){
        tiles = new HashMap<>();
    }

    public void setTiles(Map<Coord, TileView> tiles){
        this.tiles = tiles;
    }

    public void setMax(Coord maxPos){
        this.maxPos = maxPos;
    }


    public Coord maxPos(){
        return maxPos;
    }

    public TileView getPosition(Coord coord){
        for(Map.Entry entry : tiles.entrySet()){
            if(entry.getKey().equals(coord)){
                return (TileView)entry.getValue();
            }
        }
         throw new InvalidParameterException("This coord does not exists");
    }
}
