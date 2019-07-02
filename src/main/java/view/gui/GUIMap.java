package view.gui;

import board.Coord;
import uid.DamageableUID;
import uid.TileUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TargetView;
import viewclasses.TileView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GUIMap extends JPanel {



    public GUIMap(){}

    /**
     * Return a list of possible coordinates to choice as target for any kind of action.
     * @param targets is the list of the possible targets.
     * @return a list of selectable coordinates.
     */
    public List<Coord> getTargetCoordinates(List<TargetView> targets, GameMapView gmv){
        List<TileUID> tiles;
        List<DamageableUID> actors;
        List<Coord> coords = new ArrayList<>();

        for(TargetView target: targets){
            tiles = target.getTileUIDList();
            actors = target.getDamageableUIDList();
            if(!tiles.isEmpty()) {
                for (TileView tw : gmv.allTiles()) {
                    if (tiles.contains(tw.uid()))
                        coords.add(gmv.getCoord(tw));
                }
            }
            if(!actors.isEmpty()) {
                for (ActorView av : gmv.players()) {
                    if (actors.contains(av.uid())) coords.add(gmv.getCoord(av.position()));
                }
            }
        }
        return coords;
    }

    /**
     * Simple method to set a button based on the action to be applied.
     * @param question based on the kind of action to be executed.
     */
    public abstract void clickableButton(List<TargetView> targets, String question, boolean single, boolean optional);

    public static boolean isNotNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}
