package board;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import controller.GameMode;
import gamemanager.DominationMode;
import gamemanager.GameBuilder;
import genericitems.Tuple3;
import genericitems.Tuple4;
import grabbables.*;
import player.Actor;
import player.Pawn;
import uid.DamageableUID;
import uid.TileUID;
import uid.RoomUID;
import viewclasses.ActorView;
import viewclasses.GameMapView;
import viewclasses.TileView;

/**
 * The logical container of all elements of the map, room, tile, pawns, munition cards and grabbable weapons cards
 */
public class GameMap {

    /*
    Attributes
     */
    /**
     * Holds the length and width of the Map
     */
    private final Coord maxPos;

    /**
     * Map between RoomUID and the Room Class
     */
    private final Map<RoomUID, Room> roomUIDMap;

    /**
     * Map between TileUID and the Tile Class
     */
    private final Map<TileUID, Tile> tileUIDMap;

    /**
     * Stores the absolute position of each Tile. Should not be used to access to TileUID
     */
    private final List<TileUID> position;

    /**
     * Map between DamageableUID and Damageable Class
     */
    private final Map<DamageableUID, Pawn> damageableUIDMap;

    /**
     * A tile used to store dead players waiting to be spawned
     */
    private final TileUID emptyTile;


    private final Deck<Weapon> deckOfWeapon;
    private final Deck<AmmoCard> deckOfAmmoCard;
    private final Deck<grabbables.PowerUp> deckOfPowerUp;

    /*
    Constructors
     */

    /**
     * Private constructor, to build a map gameMapFactory must be used
     */
    private GameMap (
            Map<RoomUID, Room> roomUIDMap,
            Map<TileUID, Tile> tileUIDMap,
            List<TileUID> position,
            Coord maxPos,
            int numOfPlayer,
            Tuple3<Deck<Weapon>, Deck<AmmoCard>, Deck<grabbables.PowerUp>> cards) {
        this.roomUIDMap = roomUIDMap;
        this.tileUIDMap = tileUIDMap;
        this.position = position;
        this.maxPos = maxPos;

        this.deckOfWeapon = cards.x;
        this.deckOfAmmoCard = cards.y;
        this.deckOfPowerUp = cards.z;

        this.emptyTile = new TileUID();
        this.damageableUIDMap = buildPawnForPlayers(this, numOfPlayer);
        tileUIDMap.values().forEach(x -> x.setMap(this, damageableUIDMap));

        refill();
    }

    /**
     * Constructor of the GameMap object
     * @param path path of the map file
     * @param numOfPlayer the number of player that join the game
     * @param cards a Tuple containing the Decks: deck of Weapon, deck of Ammo and deck of PowerUp
     * @return the GameMap object
     */
    public static GameMap gameMapFactory(   GameMode gameMode,
                                            String path,
                                            int numOfPlayer,
                                            Tuple3<Deck<Weapon>, Deck<AmmoCard>, Deck<grabbables.PowerUp>> cards)
    {

        Tuple4<
                Map<RoomUID, Room>,
                Map<TileUID, Tile>,
                List<TileUID>,
                Coord
                > res = ParserMap.parseMap(gameMode.equals(GameMode.NORMAL), path);

        return new GameMap(res.x, res.y, res.z, res.t, numOfPlayer, cards);
    }

    /*
    Methods called from outside
     */

    /**
     * Creates a sandbox used to test the validity of any action
     * @return the Sandbox
     */
    public Sandbox createSandbox(DamageableUID pov) {
        return new Sandbox(this, pov);
    }

    /**
     * Returns the Tile Object given a TileUID
     *
     * @param tileID TileUID of the wanted Tile
     * @return the Tile corresponding to the TileUID
     * @throws NoSuchElementException If no Tile is found, an exception is returned
     */
    public Tile getTile(TileUID tileID) {
        if (tileUIDMap.containsKey(tileID) && tileUIDMap.get(tileID) != null)
            return tileUIDMap.get(tileID);
        else
            throw new NoSuchElementException("This TileUID does not exists");
    }

    /**
     * Returns the Room Object given a RoomUID
     *
     * @param roomID RoomUID of the wanted Room
     * @return the Room corresponding to the RoomUID
     * @throws NoSuchElementException If no Room is found, an exception is returned
     */
    public Room getRoom(RoomUID roomID) {
        if (roomUIDMap.containsKey(roomID))
            return roomUIDMap.get(roomID);
        else
            throw new NoSuchElementException("This RoomUID does not exists");
    }

    /**
     * Returns the Damageable Object given a DamageableUID
     *
     * @param damageableUID DamageableUID of the wanted Damageable
     * @return the Damageable corresponding to the DamageableUID
     * @throws NoSuchElementException If no Damageable is found, an exception is returned
     */
    public Pawn getPawn(DamageableUID damageableUID) {
        if (damageableUIDMap.containsKey(damageableUID)) {
            return damageableUIDMap.get(damageableUID);
        }
        else
            throw new NoSuchElementException("This DamageableUID does not exists");
    }

    /**
     * Returns the TileUID Object given a Coordinate
     *
     * @param coord Coordinate of the wanted Tile. Upper left Tile has Coord = (0,0)
     * @return the TileUID corresponding to the Coordinate. If no Tile is found, an empty Optional is returned
     * @throws NoSuchElementException If no Coord is found, an exception is returned
     */
    public TileUID getPosition(Coord coord) {
        int pos = coord.getX() * maxPos.getX() + coord.getY();
        if (pos < maxPos.getX() * maxPos.getY()
                && coord.getX() <= maxPos.getX()
                && coord.getY() <= maxPos.getY()
                && allTiles().contains(position.get(pos))
        )
            return position.get(pos);
        else
            throw new NoSuchElementException("This Coord does not exists");
    }

    /**
     *
     * @return A Set containing all the Tiles in the map
     */
    public Set<TileUID> allTiles(){
        HashSet<TileUID> ret = new HashSet<>();
        for(Map.Entry t : tileUIDMap.entrySet()){
            if(t.getValue() != null)
                ret.add((TileUID)t.getKey());
        }
        return ret;
    }

    /**
     * @param tile the Tile requested
     * @return returns the Coord of a given TileUID
     */
    public Coord getCoord(TileUID tile){
        return new Coord(position.indexOf(tile) / maxPos.getX() , position.indexOf(tile) % maxPos.getX());
    }

    /**
     * Returns the neighbors of the cell
     *
     * @param tile    the source tile
     * @param logical if true don't go through walls, if false do
     * @return A map of neighboring TileUIDs paired with the direction
     */
    public Map<Direction, TileUID> neighbors(TileUID tile, boolean logical) {
        return getTile(tile).getMapOfNeighbor(!logical);
    }

    /**
     * Returns all the pawns (player or domination points) contained in the tile
     *
     * @param tile The tileID of the Tile requested
     * @return a Collection containing all the Damageable unit in the tile
     */
    public Collection<DamageableUID> containedPawns(TileUID tile) {
        return damageableUIDMap.entrySet()
                .stream().filter(i -> i.getValue().getTile().equals(tile))
                .map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Returns the room containing the tile
     *
     * @param tile tile requested
     * @return the RoomUID where the tile is
     */
    public RoomUID room(TileUID tile) {
        return getTile(tile).getRoom();
    }

    /**
     * Returns the tile where the pawn is
     *
     * @param pawn pawn whose tile is requested
     * @return the tile containing the pawn
     */
    public TileUID tile(DamageableUID pawn) {
        return damageableUIDMap.get(pawn).getTile();
    }

    /**
     * Returns all the tiles inside a room
     *
     * @param room The room
     * @return All the tiles in the room
     */
    Collection<TileUID> tilesInRoom(RoomUID room) {
        return getRoom(room).getTiles();
    }

    /**
     * This function gets all the cells within a certain distance of the cell, including the cell itself
     * <p>
     * The result is a Set and an arbitrary range of (Logical in this case) distances [a,b] can be obtained by:
     * <p>
     * getSurroundings(False, b).removeAll(getSurroundings(False, a) )
     *
     * @param physical True for physical, False for logical
     * @param range    The depth of the depth first search to run.
     *                 The function will return **all** cells at a distance less or equal this parameter
     *                 <p>
     *                 If range is 0 the correct behaviour is to return this.tileID
     *                 If less than 0 return an empty set
     * @param tile  Tile identifier
     * @return The return value is a set instead of a Collection since it is not supposed to be ordered nor to have duplicates in it
     */
    Set<TileUID> getSurroundings(Boolean physical, Integer range, TileUID tile) {
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> border = new HashSet<>();
        Set<TileUID> newBorder = new HashSet<>();
        if (range < 0) return ret;
        ret.add(tile);
        border.add(tile);
        for (int i = 0; i < range; i++) {
            for (TileUID t : border) {
                newBorder.addAll(neighbors(t, !physical).values());
                ret.addAll(newBorder);
            }
            border = new HashSet<>(newBorder);
            newBorder.clear();
        }
        return ret;
    }

    /**
     * Finds all the Tiles that are visible from itself
     *
     * @param tile Tile identifier
     * @return A Collection containing all the visible Tiles
     */
    Collection<TileUID> getVisible(TileUID tile) {
        Set<TileUID> ret = new HashSet<>();
        Set<TileUID> surr = getSurroundings(false, 1, tile);
        for (TileUID t : surr) {
            ret.addAll(tilesInRoom(room(t)));
        }
        return ret;
    }

    /**
     * Refill all the Tiles with cards.
     * Spawn point will receive up to 3 Weapon, non-Spawn point will receive an Ammo Card
     */
    public void refill(){
        if(deckOfWeapon == null || deckOfAmmoCard == null)
            return;
        for(TileUID tile : allTiles()){
            if(getTile(tile).spawnPoint())
                deckOfWeapon.take(3 - getGrabbable(tile).size())
                    .forEach(x -> addGrabbable(tile, x));
            else
                deckOfAmmoCard.take(1 - getGrabbable(tile).size())
                        .forEach(x -> addGrabbable(tile, x));
        }
    }

    /**
     * Returns a Set containing all the Grabbable elements in this Tile
     *
     * @return A Set containing all the Grabbable elements
     */
    public Set<Grabbable> getGrabbable(TileUID tile) {
        return new HashSet<>(getTile(tile).getGrabbable());
    }

    /**
     * Adds a Grabbable elements to the Tile, and checks if the Card is correct
     *
     * @param tile      the tile where the card must be put
     * @param grabbable the card
     */
    public void addGrabbable(TileUID tile, Grabbable grabbable) {
        if (getTile(tile).spawnPoint()
                && !grabbable.getWeapon().isEmpty()
                && deckOfWeapon.isPicked(grabbable.getWeapon().iterator().next()))
            getTile(tile).addGrabbable(grabbable);
        else if (!getTile(tile).spawnPoint()
                && !grabbable.getCard().isEmpty()
                && deckOfAmmoCard.isPicked(grabbable.getCard().iterator().next()))
            getTile(tile).addGrabbable(grabbable);
        else
            throw new InvalidParameterException("The card cannot be added");
    }

    /**
     * Removes the element from the Grabbable set. If there is not this element, throws a NoSuchElementException
     *
     * @param grabbable The identifier of the grabbable item
     * @throws NoSuchElementException If this GrabbableUID is not found, an exception is returned
     */
    public Grabbable pickUpGrabbable(TileUID tile, Grabbable grabbable) {
        getTile(tile).pickUpGrabbable(grabbable);
        return grabbable;
    }

    /**
     * Removes and returns a PowerUp card picked from the Deck
     * @return A PowerUp card
     */
    public PowerUp pickUpPowerUp(){
        return deckOfPowerUp.next();
    }

    /**
     * Allows the player to discard a PowerUp card.
     * If the Card is not discardable (if it is in the deck or in the stash) an InvalidParameterException is thrown
     * @param grabbable the Card to be discarded
     */
    public void discardPowerUp(PowerUp grabbable){
        if(deckOfPowerUp.isPicked(grabbable)){
            deckOfPowerUp.discard(grabbable);
        }
        else
            throw new InvalidParameterException("This PowerUp cannot be discarded");
    }

    /**
     * Allows the player to discard an AmmoCard.
     * If the Card is not discardable (if it is in the deck or in the stash) an InvalidParameterException is thrown
     * @param grabbable the Card to be discarded
     */
    public void discardAmmoCard(AmmoCard grabbable){
        if(deckOfAmmoCard.isPicked(grabbable)){
            deckOfAmmoCard.discard(grabbable);
        }
        else
            throw new InvalidParameterException("This AmmoCard cannot be discarded");
    }

    /**
     * Allow the player to discard a Weapon. Needed if the stash is empty
     * @param tile the Tile where the weapon must be put
     * @param grabbable The weapon that has to be discarded
     */
    public void discardWeapon(TileUID tile, Weapon grabbable){
        if(deckOfWeapon.isPicked(grabbable)){
            addGrabbable(tile, grabbable);
        }
        else
            throw new InvalidParameterException("This Weapon cannot be discarded");
    }

    /**
     * Returns a collection with all the DamageableUID contained in the tile
     *
     * @return A collections containing the DamageableUID in the tile
     */
    public Collection<DamageableUID> getDamageable(TileUID tile) {
        return containedPawns(tile);
    }

    /**
     * Returns all the Damageable Objects (Pawns and Domination points) in the map
     * @return A set containing all the Damageable UID
     */
    public Set<DamageableUID> getDamageable(){
        return new HashSet<>(damageableUIDMap.keySet());
    }

    /**
     * @return The empty Tile used to store dead pawns
     */
    public TileUID getEmptyTile(){
        return emptyTile;
    }


    /**
     *
     * @return True iif the Deck of weapon is empty
     */
    public boolean emptyWeaponDeck(){
        try{
            deckOfWeapon.next();
        }
        catch (NoSuchElementException e){
            return true;
        }
        return false;
    }
    
    private Map<DamageableUID, Pawn> buildPawnForPlayers(GameMap map, int numOfPlayer){
        Map<DamageableUID, Pawn> res = new HashMap<>();
        Pawn p;
        DamageableUID uid;
        for(int i=0; i<numOfPlayer; i++){
            uid = new DamageableUID();
            p = new Pawn(uid, map.emptyTile, this);
            res.put(uid, p);
        }
        return res;
    }

    /**
     * Generates a copy of the Map for Serialization
     * @return the GameMapView object
     */
    public GameMapView generateView(DamageableUID pointOfView) {

        GameMapView gameMapView = new GameMapView();

        List<ActorView> players = new ArrayList<>();
        List<ActorView> players2 = new ArrayList<>();
        ActorView you = new ActorView();
        for(Actor a : GameBuilder.get().getActorList()) {
            DamageableUID actor = a.pawnID();
            if (pointOfView.equals(actor))
                you = new ActorView(getPawn(actor).getDamageableUID());
            players.add(new ActorView(getPawn(actor).getDamageableUID()));
        }

        gameMapView.setYou(you);
        gameMapView.setPlayers(players);

        you = getPawn(you.uid()).generateView(gameMapView, pointOfView, pointOfView);
        for(Actor a : GameBuilder.get().getActorList()) {
            DamageableUID uid = a.pawnID();
            players2.add(getPawn(uid).generateView(gameMapView, uid, pointOfView));
        }
        gameMapView.setYou(you);
        gameMapView.setPlayers(players2);

        Map<Coord, TileView> tiles = new HashMap<>();
        for (TileUID tile : position) {
            if (allTiles().contains(tile))
                tiles.put(getCoord(tile), getTile(tile).generateView(gameMapView));
            //else
            //    tiles.put(getCoord(tile), null);
        }

        gameMapView.setTiles(tiles);
        gameMapView.setMax(maxPos);

        gameMapView.setGameMode(GameBuilder.get().getGameMode());



        if (gameMapView.gameMode().equals(GameMode.DOMINATION)) {
            gameMapView.setDominationPointActor(GameBuilder.get().getDominationPointActor());
            gameMapView.setSkullBox(GameBuilder.get().getScoreboard().getSkullBox());
            gameMapView.setSpawnTracker(((DominationMode) GameBuilder.get().getScoreboard()).getSpawnTracker());
        }
        else
            gameMapView.setSkullBox(GameBuilder.get().getScoreboard().getSkullBox());
        return gameMapView;
    }
}
