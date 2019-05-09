package viewclasses;

import board.Direction;
import grabbables.Grabbable;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TileView implements Serializable {

    private Color color;
    private boolean spawnPoint;
    private Map<Direction, String> nearTiles;
    //TODO: check if this is serialized correctly
    private List<ActorView> players;
    //TODO: implement grabbable sending
    //private ArrayList<Grabbable> grabbable;

    public TileView(){
        players = new ArrayList<>();
        //grabbable = new ArrayList<>();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setSpawnPoint(boolean spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public void setNearTiles(Map<Direction, String> nearTiles) {
        this.nearTiles = nearTiles;
    }

    public void setPlayers(List<ActorView> players) {
        this.players = players;
    }

    /*public void setGrabbable(ArrayList<Grabbable> grabbable) {
        this.grabbable = grabbable;
    }*/



    public Color color() {
        return color;
    }

    public boolean spawnPoint(){
        return spawnPoint;
    }

    public Map<Direction, String> nearTiles() {
        return nearTiles;
    }

    public List<ActorView> players() {
        return players;
    }

    /*public List<Grabbable> grabbable() {
        return grabbable;
    }*/
}

