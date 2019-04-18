/*package board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Pawn;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest{
    private GameMap map;

    private RoomUID room;

    private Room roomObj;

    private Tile tile;
    private Tile tileUp;
    private Tile tileDown;
    private Tile tileLeft;
    private Tile tileRight;

    private TileUID thisTile;
    private TileUID tUp;
    private TileUID tDown;
    private TileUID tLeft;
    private TileUID tRight;

    private DamageableUID pawn1 = new DamageableUID();
    private DamageableUID pawn2 = new DamageableUID();
    private Pawn p1 = new Pawn();
    private Pawn p2 = new Pawn();


    @BeforeEach
    void setup(){

        // stanza centrale con 4 laterali
        //UP RIGHT visibili
        //LEFT non visibile
        //DOWN non presente

        room = new RoomUID();
        thisTile = new TileUID();

        tUp = new TileUID();
        tDown = new TileUID();
        tLeft = new TileUID();
        tRight = new TileUID();

        NeightTile neightTileUp = new NeightTile(tUp, true);
        //NeightTile neightTileDown = new NeightTile(tDown, true);
        NeightTile neightTileLeft = new NeightTile(tLeft, false);
        NeightTile neightTileRight = new NeightTile(tRight, true);

        Map<Direction, NeightTile> neighbors = new HashMap<>();
        neighbors.put(Direction.UP, neightTileUp);
        //neighbors.put(Direction.DOWN, neightTileDown);
        neighbors.put(Direction.LEFT, neightTileLeft);
        neighbors.put(Direction.RIGHT, neightTileRight);

        Map<Direction, NeightTile> neighborsOfUp = Map.of(Direction.DOWN, new NeightTile(thisTile, true));
        //Map<Direction, NeightTile> neighborsOfDown = Map.of(Direction.UP, new NeightTile(thisTile, true));
        Map<Direction, NeightTile> neighborsOfLeft = Map.of(Direction.RIGHT, new NeightTile(thisTile, false));
        Map<Direction, NeightTile> neighborsOfRight = Map.of(Direction.LEFT, new NeightTile(thisTile, true));

        tileUp = new Tile(null, room, tUp, neighborsOfUp);
        //tileDown = new Tile(null, room, tDown, neighborsOfDown);
        tileDown = new Tile(null, room, tDown, null);
        tileLeft = new Tile(null, room, tLeft, neighborsOfLeft);
        tileRight = new Tile(null, room, tRight, neighborsOfRight);
        tile = new Tile(null, room, thisTile, neighbors);

        roomObj = new Room(room, Set.of(thisTile, tUp, tDown, tRight), new Color(255,255,255));
        Map<TileUID, Tile> tileUIDMap = Map.of(thisTile,tile , tUp,tileUp , tDown,tileDown , tRight,tileRight , tLeft,tileLeft );
        Map<RoomUID, Room> roomUIDMap = Map.of(room, roomObj);

        Map<Coord, TileUID> position = Map.of(coord(0,0),thisTile, coord(1,1),tDown);

        Map<DamageableUID, Pawn> damageableUIDMap = Map.of(pawn1,p1 , pawn2,p2);

        map = new GameMap(roomUIDMap, tileUIDMap, null, null, damageableUIDMap);

        tileUp.setMap(map);        tileDown.setMap(map);        tileLeft.setMap(map);        tileRight.setMap(map);
        tile.setMap(map);
    }

    private Coord coord(int x, int y){
        return new Coord(x,y);
    }


    @Test
    void testGetters(){
        assertEquals(map.getTile(thisTile) , tile);
        assertEquals(map.getTile(tUp) , tileUp);
        assertThrows(NoSuchElementException.class , () -> map.getTile(new TileUID()));

        assertEquals(roomObj , map.getRoom(room));
        assertThrows(NoSuchElementException.class , () -> map.getRoom(new RoomUID()));

        assertEquals(p1 , map.getDamageable(pawn1));
        assertEquals(p2 , map.getDamageable(pawn2));
        assertThrows(NoSuchElementException.class , () -> map.getDamageable(new DamageableUID()));

        assertEquals(room , map.room(thisTile));
        assertEquals(room , map.room(tUp));

        assertEquals(Set.of(thisTile,tUp,tDown,tRight) , map.tilesInRoom(room));
    }


    @Test
    void testNeighbors(){
        helperTest(thisTile,true);
        helperTest(thisTile,false);
        helperTest(tUp,true);
    }
    private void helperTest(TileUID t, boolean logical){
        Map<Direction, TileUID> m;
        Map<Direction, TileUID> m1;
        m = map.neighbors(t,logical);
        m1 = map.getTile(t).getMapOfNeighbor(!logical);
        for(Direction d : m.keySet()){
            for(Direction d1 : m.keySet()) {
                if(d.equals(d1))
                    assertEquals(m.get(d) , m1.get(d1));
            }
        }
    }

    @Test
    void testPawn(){
        assertThrows(NoSuchElementException.class , () -> map.tile(pawn1));
        map.getTile(thisTile).addDamageable(pawn1);
        assertEquals(thisTile ,  map.tile(pawn1));
        assertEquals(Set.of(pawn1) , map.containedPawns(thisTile));
        tile.removeDamageable(pawn1);
        assertThrows(NoSuchElementException.class , () -> map.tile(pawn1));
        assertTrue(map.containedPawns(thisTile).isEmpty());
        assertTrue(map.containedPawns(tLeft).isEmpty());
    }

    @Test
    void testCoord(){
        assertEquals(0 , new Coord(0,5).getX());
        assertEquals(5 , new Coord(0,5).getY());
    }

    @Test
    void testSandbox(){
        //TODO: add Pawn
        /*Sandbox s = null;
        s = map.createSandbox();
        assertNotNull(s);*/
    //}


//}
//*/