package board;

import gamemanager.GameBuilder;
import genericitems.Tuple3;
import grabbables.AmmoCard;
import grabbables.Deck;
import grabbables.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class newGameMapTest {
    private GameMap map;
    private Deck<AmmoCard> ammoCardDeck;
    private Deck<PowerUp> powerUpDeck;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String mapPath = "src/resources/map1.txt";
        String ammoPath = "src/resources/ammoTile.txt";
        String powerPath = "src/resources/powerUp.txt";
        try {
            builder = new GameBuilder(
                    mapPath, null, powerPath, ammoPath, 3);
        }
        catch (FileNotFoundException e){
        }
        map = builder.getMap();
        ammoCardDeck = builder.getDeckOfAmmoCard();
        powerUpDeck = builder.getDeckOfPowerUp();
    }

    @Test
    void neighborTest(){
        helperTest(map.getPosition(new Coord(0,0)),true);
        helperTest(map.getPosition(new Coord(0,0)),false);
        helperTest(map.getPosition(new Coord(1,1)),true);
        assertTrue(map.neighbors(map.getPosition(new Coord(0,3)),false).isEmpty());
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
    void pawnTest(){
        assertThrows(NoSuchElementException.class , () -> map.tile(new DamageableUID()));

        DamageableUID[] listD = new DamageableUID[map.getDamageable().size()];
        map.getDamageable().toArray(listD);

        map.addDamageable(map.getPosition(new Coord(0,0)) , listD[0]);
        assertEquals(map.getPosition(new Coord(0,0)) ,  map.tile(listD[0]));
        assertEquals(Set.of(listD[0]) ,  map.containedPawns(map.getPosition(new Coord(0,0))));

        map.removeDamageable(map.getPosition(new Coord(0,0)) , listD[0]);
        assertThrows(NoSuchElementException.class , () -> map.tile(new DamageableUID()));
        assertThrows(NoSuchElementException.class , () -> map.removeDamageable(map.getPosition(new Coord(1,1)), listD[0]));
        assertTrue(map.containedPawns(map.getPosition(new Coord(0,0))).isEmpty());
        assertTrue(map.containedPawns(map.getPosition(new Coord(0,2))).isEmpty());
        assertTrue(map.containedPawns(map.getPosition(new Coord(0,3))).isEmpty());


        map.addDamageable(map.getPosition(new Coord(2,2)) , listD[1]);
        map.addDamageable(map.getPosition(new Coord(2,2)) , listD[2]);
        assertEquals(2 , map.getDamageable(map.getPosition(new Coord(2,2))).size());
        assertTrue(map.getDamageable(map.getPosition(new Coord(2,2))).containsAll(Set.of(listD[1],listD[2])));

        assertTrue(map.containedPawns(map.getPosition(new Coord(2,2))).contains(listD[1]));
        assertFalse(map.containedPawns(map.getPosition(new Coord(1,2))).contains(listD[1]));
        assertFalse(map.containedPawns(map.getPosition(new Coord(0,2))).contains(listD[0]));
        assertThrows(NoSuchElementException.class , () -> map.getPawn(new DamageableUID()));

    }

    @Test
    void sandboxTest(){
        Sandbox s = map.createSandbox();
        assertNotNull(s);
    }

    @Test
    void getterTest(){
        assertThrows(NoSuchElementException.class , () -> map.getTile(new TileUID()));

        assertThrows(NoSuchElementException.class , () -> map.getRoom(new RoomUID()));

        assertThrows(NoSuchElementException.class , () -> map.getPosition(new Coord(0,5)));

        TileUID t = map.getPosition(new Coord(0,0));
        assertTrue(map.getRoom(map.room(t)).getTiles().contains(t));
    }

    @Test
    void neighborTest2(){
        assertEquals(map.getTile(map.getPosition(new Coord(2,3))).getMapOfNeighbor(true) ,
                map.neighbors(map.getPosition(new Coord(2,3)), true));
        assertEquals(map.getTile(map.getPosition(new Coord(2,3))).getMapOfNeighbor(false) ,
                map.neighbors(map.getPosition(new Coord(2,3)), true));
        assertEquals(map.getTile(map.getPosition(new Coord(0,3))).getMapOfNeighbor(true) ,
                map.neighbors(map.getPosition(new Coord(0,3)), true));

        assertTrue(map.neighbors(map.getPosition(new Coord(0,3)), true).isEmpty());

        assertEquals(2, map.neighbors(map.getPosition(new Coord(2,3)), true).size());
    }


    @Test
    void tilesInRoomTest(){
        HashSet<TileUID> res = new HashSet<>();
        res.add(map.getPosition(new Coord(2,1)));
        res.add(map.getPosition(new Coord(2,2)));
        res.add(map.getPosition(new Coord(2,2)));
        assertEquals(res , map.tilesInRoom(map.room(map.getPosition(new Coord(2,2)))));
        assertEquals(res , map.tilesInRoom(map.getTile(map.getPosition(new Coord(2,2))).getRoom()));
    }

    @Test
    void getSurroundingsTest(){
        Collection<TileUID> res;

        res = map.getSurroundings(false, 3, map.getPosition(new Coord(0,0)));
        assertEquals(7, res.size());

        res = map.getSurroundings(false, 5, map.getPosition(new Coord(0,0)));
        assertEquals(10, res.size());

        res = map.getSurroundings(true, 2, map.getPosition(new Coord(2,2)));
        assertEquals(7, res.size());

        res = map.getSurroundings(true, 0, map.getPosition(new Coord(2,2)));
        assertEquals(1, res.size());
    }

    @Test
    void getVisibleTest() {
        ArrayList<TileUID> res = new ArrayList<>();
        res.add(map.getPosition(new Coord(0, 0)));
        res.add(map.getPosition(new Coord(0, 1)));
        res.add(map.getPosition(new Coord(0, 2)));
        assertTrue(map.getVisible(map.getPosition(new Coord(0, 1))).containsAll(res));
        assertTrue(map.getVisible(map.getPosition(new Coord(0, 1)))
                .containsAll(map.tilesInRoom(map.room(map.getPosition(new Coord(0, 0))))));

        res.clear();
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(0, 0)))));
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(1, 0)))));
        res.addAll(map.tilesInRoom(map.room(map.getPosition(new Coord(1, 3)))));
        assertTrue(map.getVisible(map.getPosition(new Coord(1, 2))).containsAll(res));
    }

    @Test
    void emptyGrabbableTest(){
        for(TileUID t : map.allTiles()){
            assertTrue(map.getGrabbable(t).isEmpty());
        }
    }

    @Test
    void addGrabbableTest(){
        map.addGrabbable(map.getPosition(new Coord(0,0)), ammoCardDeck.next());
        assertThrows(InvalidParameterException.class , () -> map.addGrabbable(map.getPosition(new Coord(0,0)), powerUpDeck.next()));
    }
}