package actions.utils;

import genericitems.Tuple3;

import java.awt.*;
import java.io.Serializable;

/**
 * This class will be used to represent the colors of available ammunition as well as the spawn points
 */
public enum AmmoColor implements Serializable {
    BLUE,
    RED,
    YELLOW;

    public Tuple3<Integer, Integer, Integer> toRGB(){
        if(this.equals(AmmoColor.BLUE))
            return new Tuple3<>(0,0,255);
        if(this.equals(AmmoColor.RED))
            return new Tuple3<>(255,0,0);
        if(this.equals(AmmoColor.YELLOW))
            return new Tuple3<>(255,255,0);
        return new Tuple3<>(0,0,0);
    }

    public Color toColor(){
        switch (AmmoColor.this){
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;

                default:
                    return Color.white;
        }
    }
}