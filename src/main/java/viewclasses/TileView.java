package viewclasses;

import board.Direction;
import uid.TileUID;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * This class contains the Tile that is used by the view and transmitted from the server to the client
 */
public class TileView implements Serializable {

    private TileUID uid;
    private Color color;
    private boolean spawnPoint;
    private Map<Direction, String> nearTiles;
    private List<ActorView> players;
    private List<WeaponView> weapons;
    private AmmoTileView ammoCard;

    public TileView(){
        players = new ArrayList<>();
        nearTiles = new EnumMap<>(Direction.class);
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

    public void setUid(TileUID uid) {
        this.uid = uid;
    }

    public void setAmmoCard(AmmoTileView ammoCard) {
        this.ammoCard = ammoCard;
    }

    public void setWeapons(List<WeaponView> weapons) {
        this.weapons = weapons;
    }


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

    public TileUID uid(){
        return uid;
    }

    public AmmoTileView ammoCard() {
        return ammoCard;
    }

    public List<WeaponView> weapons() {
        return weapons;
    }
}

