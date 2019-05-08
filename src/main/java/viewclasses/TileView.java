package viewclasses;

import board.Coord;
import board.Direction;
import board.Tile;
import grabbables.Grabbable;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TileView implements Serializable {

    public Color color;
    public boolean spawnPoint;
    public Map<Direction, String> nearTiles;
    public List<ActorView> players;
    public List<Grabbable> grabbable;

    public TileView() {}

}

