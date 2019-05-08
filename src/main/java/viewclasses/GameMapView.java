package viewclasses;

import board.Coord;
import board.GameMap;
import uid.TileUID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
public class GameMapView {
    public Map<Coord, TileView> tiles
}

public class TileView {
    public Map<Direction, TipoContorno> muri; // dove TipoContorno è un enum {Muro, porta, niente}
    public Color coloreStanza;
    // qualcosa per i grabbable che distingue se sono armi o munizioni
    List<PawnView> pawns
.....
}*/

public class GameMapView implements Serializable {
    //TODO: how to do that?
    public Map<Coord, TileView> tiles;

    public GameMapView(){
        tiles = new HashMap<>();
    }




    public TileView getPosition(Coord coord){
        return null;
    }
}
