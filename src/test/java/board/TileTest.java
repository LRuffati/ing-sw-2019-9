package board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.GrabbableUID;
import uid.RoomUID;
import uid.TileUID;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class TileTest {

    private GameMap map;

    private RoomUID room;

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

        Map<TileUID, Tile> tileUIDMap = Map.of(thisTile,tile , tUp,tileUp , tDown,tileDown , tRight,tileRight , tLeft,tileLeft );
        Map<RoomUID, Room> roomUIDMap = Map.of(room, new Room(room, Set.of(thisTile, tUp, tDown, tLeft, tRight),
                                                                                new Color(255,255,255)));

        map = new GameMap(roomUIDMap, tileUIDMap, null);

        tileUp.setMap(map);        tileDown.setMap(map);        tileLeft.setMap(map);        tileRight.setMap(map);
        tile.setMap(map);
    }

    @Test
    void testGetNeighbor(){
        Optional<TileUID> t;
        t = tile.getNeighbor(true, Direction.UP);
        assertEquals(Optional.of(tUp), t);
        assertEquals(tileUp, map.getTile(t.get()));

        t = tile.getNeighbor(false, Direction.RIGHT);
        assertEquals(Optional.of(tRight), t);
        assertEquals(tileRight, map.getTile(t.get()));

        t = tile.getNeighbor(true, Direction.LEFT);
        assertEquals(Optional.of(tLeft), t);
        assertEquals(tileLeft, map.getTile(t.get()));

        t = tile.getNeighbor(false, Direction.LEFT);
        final Optional<TileUID> t1 = t;
        assertEquals(Optional.empty(), t);
        assertThrows(NoSuchElementException.class , () -> map.getTile(t1.get()));

        t = tile.getNeighbor(true, Direction.DOWN);
        final Optional<TileUID> t2 = t;
        assertEquals(Optional.empty(), t);
        assertThrows(NoSuchElementException.class , () -> map.getTile(t2.get()));
    }

    @Test
    void testgetMapOfNeighbor(){
        Map<Direction, TileUID> m;
        m = tile.getMapOfNeighbor(true);
        for(Direction d : m.keySet()){
            assertEquals(tile.getNeighbor(true, d).get() , m.get(d));
        }
        m = tile.getMapOfNeighbor(false);
        for(Direction d : m.keySet()){
            assertEquals(tile.getNeighbor(false, d).get() , m.get(d));
        }

        final Map<Direction, TileUID> m1 = m;
        assertThrows(NoSuchElementException.class , () -> m1.get(Direction.DOWN));
        assertThrows(NoSuchElementException.class , () -> m1.get(Direction.LEFT));
    }

    @Test
    void testGrabbable(){
        Set<GrabbableUID> g;
        GrabbableUID g1 = new GrabbableUID();
        g = tile.getGrabbable();
        assertTrue(g.isEmpty());
        g.add(g1);
        assertNotEquals(g, tile.getGrabbable());
        assertThrows(NoSuchElementException.class , () -> tile.pickUpGrabbable(g1));

        tile.addGrabbable(g1);
        g = tile.getGrabbable();
        assertFalse(g.isEmpty());
        assertEquals(g, tile.getGrabbable());
        tile.pickUpGrabbable(g1);
        assertTrue(g.isEmpty());
        assertEquals(new HashSet<>() , tile.getGrabbable());
    }

    @Test
    void testDamageable(){
        Set<DamageableUID> g;
        DamageableUID g1 = new DamageableUID();
        g = (HashSet) tile.getDamageable();
        assertTrue(g.isEmpty());
        g.add(g1);
        assertNotEquals(g, tile.getDamageable());
        assertThrows(NoSuchElementException.class , () -> tile.removeDamageable(g1));

        tile.addDamageable(g1);
        g = (HashSet) tile.getDamageable();
        assertFalse(g.isEmpty());
        assertEquals(g, tile.getDamageable());
        tile.removeDamageable(g1);
        assertTrue(g.isEmpty());
        assertEquals(new HashSet<>() , tile.getDamageable());
    }

    @Test
    void testVarious(){
        //testSetMap
        GameMap m = new GameMap(null, null, null);
        assertThrows(NoSuchElementException.class , () -> m.getTile(thisTile));
        //testColor
        assertEquals(new Color(255,255,255) , tile.getColor());
        //testRoom
        assertEquals(room , tile.getRoom());
    }

    @Test
    void testGetSurroundings(){
        assertTrue(true);
        //TODO
    }

    @Test
    void testgetDirection(){
        assertTrue(true);
        //TODO
    }

    @Test
    void testgetVisible(){
        assertTrue(true);
        //TODO
    }


}