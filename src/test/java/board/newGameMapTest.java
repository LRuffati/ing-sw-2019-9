package board;

import gamemanager.GameBuilder;
import gamemanager.ParserConfiguration;
import grabbables.AmmoCard;
import grabbables.Deck;
import grabbables.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.Pawn;
import uid.DamageableUID;
import uid.RoomUID;
import uid.TileUID;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class newGameMapTest {
    private GameMap map;
    private Deck<AmmoCard> ammoCardDeck;
    private Deck<PowerUp> powerUpDeck;

    @BeforeEach
    void setup(){
        GameBuilder builder = null;
        String mapPath = ParserConfiguration.parsePath("map1Path");
        String ammoPath = ParserConfiguration.parsePath("ammoTilePath");
        String powerPath= ParserConfiguration.parsePath("powerUpPath");
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
    void nullTileTest(){
        assertThrows(NoSuchElementException.class , () -> map.getPosition(new Coord(0,3)));
        assertThrows(NoSuchElementException.class , () -> map.getPosition(new Coord(2,0)));
    }

    @Test
    void allTileTest(){
        assertEquals(12-2 , map.allTiles().size());
    }

    @Test
    void neighborTest(){
        helperTest(map.getPosition(new Coord(0,0)),true);
        helperTest(map.getPosition(new Coord(0,0)),false);
        helperTest(map.getPosition(new Coord(1,1)),true);
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

        Pawn pawn = map.getPawn(listD[0]);
        Pawn pawn1 = map.getPawn(listD[1]);
        Pawn pawn2 = map.getPawn(listD[2]);


        pawn.move(map.getPosition(new Coord(0,0)));
        assertEquals(map.getPosition(new Coord(0,0)) ,  map.tile(listD[0]));
        assertEquals(new ArrayList<>(Set.of(listD[0])) ,  map.containedPawns(map.getPosition(new Coord(0,0))));

        pawn.move(map.getPosition(new Coord(1,1)));
        assertThrows(NoSuchElementException.class , () -> map.tile(new DamageableUID()));
        assertTrue(map.containedPawns(map.getPosition(new Coord(0,0))).isEmpty());
        assertTrue(map.containedPawns(map.getPosition(new Coord(0,2))).isEmpty());


        pawn1.move(map.getPosition(new Coord(2,2)));
        pawn2.move(map.getPosition(new Coord(2,2)));
        assertEquals(2 , map.getDamageable(map.getPosition(new Coord(2,2))).size());
        assertTrue(map.getDamageable(map.getPosition(new Coord(2,2)))
                .containsAll(Set.of(listD[1],listD[2])));

        assertTrue(map.containedPawns(map.getPosition(new Coord(2,2))).contains(listD[1]));
        assertFalse(map.containedPawns(map.getPosition(new Coord(1,2))).contains(listD[1]));
        assertFalse(map.containedPawns(map.getPosition(new Coord(0,2))).contains(listD[0]));
        assertThrows(NoSuchElementException.class , () -> map.getPawn(new DamageableUID()));

    }

    @Test
    void sandboxTest(){
        Sandbox s = map.createSandbox(map.getDamageable().iterator().next());
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
    void addAndPickUpGrabbableTest(){
        TileUID tile = map.getPosition(new Coord(0,0));
        AmmoCard card = ammoCardDeck.next();

        assertThrows(InvalidParameterException.class ,
                () -> map.addGrabbable(tile, powerUpDeck.next()));
        assertThrows(NoSuchElementException.class,
                () -> map.pickUpGrabbable(tile, ammoCardDeck.next()));

        map.addGrabbable(tile, card);
        assertTrue(map.getGrabbable(tile).contains(card));
        map.pickUpGrabbable(tile, card);
        assertFalse(map.getGrabbable(tile).contains(card));
        assertTrue(ammoCardDeck.isPicked(card));

        map.discardAmmoCard(card);
        assertFalse(ammoCardDeck.isPicked(card));

    }

    @Test
    void powerUpGrabbableTest(){
        PowerUp pUp = map.pickUpPowerUp();
        assertTrue(powerUpDeck.isPicked(pUp));

        assertThrows(InvalidParameterException.class ,
                () -> map.addGrabbable(map.getPosition(new Coord(0,0)), pUp));

        map.discardPowerUp(pUp);
        assertFalse(powerUpDeck.isPicked(pUp));
    }

    @Test
    void testGrabbableException(){
        PowerUp pUp = map.pickUpPowerUp();
        map.discardPowerUp(pUp);
        assertThrows(InvalidParameterException.class, () -> map.discardPowerUp(pUp));

        map.addGrabbable(map.getPosition(new Coord(1,1)), ammoCardDeck.next());
        AmmoCard card =
                (AmmoCard)map.pickUpGrabbable(
                        map.getPosition(new Coord(1,1)),
                        (map.getGrabbable(map.getPosition(new Coord(1,1))).iterator().next()
                        )
                );
        final AmmoCard ammo = card;
        map.discardAmmoCard(ammo);
        assertThrows(InvalidParameterException.class, () -> map.discardAmmoCard(ammo));

        card = ammoCardDeck.next();
        final AmmoCard ammo1 = card;
        ammoCardDeck.discard(ammo1);
        assertThrows(InvalidParameterException.class, () -> map.discardAmmoCard(ammo1));
    }

    @Test
    void testEmptyWeaponDeckWithoutWeapon(){
        Deck<Integer> deck= new Deck<>(Set.of(1,2,3,4,5));
        assertFalse(emptyWeaponDeck(deck));
        deck.take(7);
        assertTrue(emptyWeaponDeck(deck));
    }

    private boolean emptyWeaponDeck(Deck deck){
        try{
            deck.next();
        }
        catch (NoSuchElementException e){
            return true;
        }
        return false;
    }
}