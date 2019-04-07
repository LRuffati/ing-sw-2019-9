package board;

import org.junit.jupiter.api.BeforeEach;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@BeforeEach
class TileTest {

    void setup(){
        GameMap map;
        RoomUID room = new RoomUID();
        TileUID thisTile = new TileUID();

        TileUID tUp = new TileUID();
        TileUID tDown = new TileUID();
        TileUID tLeft = new TileUID();
        TileUID tRight = new TileUID();

        NeightTile neightTileUp = new NeightTile(tUp, true);
        NeightTile neightTileDown = new NeightTile(tDown, true);
        NeightTile neightTileLeft = new NeightTile(tLeft, true);
        NeightTile neightTileRight = new NeightTile(tRight, true);

        Map<Direction, NeightTile> neighbors = new HashMap<>();
        neighbors.put(Direction.UP, neightTileUp);
        neighbors.put(Direction.DOWN, neightTileDown);
        neighbors.put(Direction.LEFT, neightTileLeft);
        neighbors.put(Direction.RIGHT, neightTileRight);

        Map<Direction, NeightTile> neighborsOfUp = Map.of(Direction.DOWN, new NeightTile(thisTile, true));
        Map<Direction, NeightTile> neighborsOfDown = Map.of(Direction.DOWN, new NeightTile(thisTile, true));
        Map<Direction, NeightTile> neighborsOfLeft = Map.of(Direction.DOWN, new NeightTile(thisTile, true));
        Map<Direction, NeightTile> neighborsOfRight = Map.of(Direction.DOWN, new NeightTile(thisTile, true));

        Tile tileUp = new Tile(null, room, tUp, neighborsOfUp);
        Tile tileDown = new Tile(null, room, tDown, neighborsOfDown);
        Tile tileLeft = new Tile(null, room, tLeft, neighborsOfLeft);
        Tile tileRight = new Tile(null, room, tRight, neighborsOfRight);
        Tile tile = new Tile(null, room, thisTile, neighbors);

        Map<TileUID, Tile> tileUIDMap = Map.of(thisTile, tile, tUp, tileUp, tDown, tileDown, tLeft, tileLeft, tRight, tileRight);
        Map<RoomUID, Room> roomUIDMap = Map.of(room, new Room(room, Set.of(thisTile, tUp, tDown, tLeft, tRight),
                                                                                new Color(255,255,255)));

        map = new GameMap(roomUIDMap, tileUIDMap, null);

        tileUp.setMap(map);        tileDown.setMap(map);        tileLeft.setMap(map);        tileRight.setMap(map);
        tile.setMap(map);
    }

}