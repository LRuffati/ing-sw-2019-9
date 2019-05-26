package controllerclient;

import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.TileView;

import java.awt.*;
import java.util.Collection;

public class TargetViewClient {

    private String gameMapViewId;
    private Collection<DamageableUID> damageableUIDList;
    private Collection<TileUID> tileUIDList;

    public TargetViewClient(TargetView targetView){
        this.gameMapViewId = targetView.getGameMapViewId();
        this.damageableUIDList = targetView.getDamageableUIDList();
        this.tileUIDList = targetView.getTileUIDList();
    }

    public String getGameMapViewId() {
        return gameMapViewId;
    }

    public Collection<DamageableUID> getDamageableUIDList() {
        return damageableUIDList;
    }

    public Collection<TileUID> getTileUIDList() {
        return tileUIDList;
    }



    public GameMapView applyEffect(GameMapView gameMap){
        for(TileView t: gameMap.allTiles()){
            if(!tileUIDList.contains(t.uid())){
                t.setColor(Color.DARK_GRAY);
            }
        }
        for(ActorView a : gameMap.players()){
            if(!damageableUIDList.contains(a.uid())){
                a.setColor(Color.lightGray);
            }
        }
        return gameMap;
    }
}
