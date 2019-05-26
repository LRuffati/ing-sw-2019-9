package controllerclient;

import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.TileView;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class ApplyTargetREADME {

    //TODO: add this method in the correct place in CLIMAP
    //todo: should stay inside the chooseTarget execution


    public GameMapView applyTarget(GameMapView gameMap, List<TargetView> targetViewList){
        for(TargetView target :  targetViewList) {
            Collection<TileUID> tiles = target.getTileUIDList();
            Collection<DamageableUID> actors = target.getDamageableUIDList();
            for (TileView t : gameMap.allTiles()) {
                if (!tiles.contains(t.uid())) {
                    t.setColor(Color.DARK_GRAY);
                }
            }
            for (ActorView a : gameMap.players()) {
                if (!actors.contains(a.uid())) {
                    a.setColor(Color.lightGray);
                }
            }
        }
        return gameMap;
    }
}
