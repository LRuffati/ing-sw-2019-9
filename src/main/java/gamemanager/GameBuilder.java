package gamemanager;

import board.Coord;
import board.DominationPointTile;
import board.GameMap;
import controller.GameMode;
import genericitems.Tuple3;
import grabbables.*;
import player.Actor;
import player.DominationPoint;
import uid.DamageableUID;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to create a new Game. It creates the GameMap, all the Decks and the Scoreboard.
 * Everything is built in the constructor, attributes have to be accessed through methods.
 */
public class GameBuilder {

    private static GameBuilder instance;

    public static GameBuilder get(){
        return instance;
    }

    private Deck<Weapon> deckOfWeapon;
    private Deck<PowerUp> deckOfPowerUp;
    private Deck<AmmoCard> deckOfAmmoCard;
    private List<Actor> actorList;
    private GameMap map;
    private Scoreboard scoreboard;
    private String mapName = "map1";

    private Map<Coord, Actor> dominationPointActor = new HashMap<>();
    private GameMode gameMode;

    /**
     * Constructor of the class. It builds all the modules needed for the game to start.
     * Requires the configuration files for all the components.
     * The syntax of the files is not checked, so new files must be written very accurately.
     *
     * If parameters are null it recovers files from resources repository.
     *
     * @param gameMode the {@link controller.GameMode type} of game
     * @param mapPath Path of the map file.
     * @param weaponPath Path of the Weapons file.
     * @param powerUpPath Path of the PowerUps file
     * @param ammoCardPath Path of the AmmoCard file.
     * @param numOfPlayer Number of players that will join the game.
     */
    public GameBuilder( GameMode gameMode,
                        String mapPath,
                        String weaponPath,
                        String powerUpPath,
                        String ammoCardPath,
                        int numOfPlayer)
    {

        this.gameMode = gameMode;

        deckOfWeapon = parserWeapon(weaponPath);
        deckOfPowerUp = parserPowerUp(powerUpPath);
        deckOfAmmoCard = parserAmmoTile(ammoCardPath);

        Tuple3<Deck<Weapon>, Deck<AmmoCard>, Deck<PowerUp>> decks = new Tuple3<>(deckOfWeapon, deckOfAmmoCard, deckOfPowerUp);

        map = parserMap(gameMode, mapPath, numOfPlayer, decks);

        scoreboard = gameMode.equals(GameMode.NORMAL) ? new Scoreboard() : new DominationMode();

        actorList = buildActor(map);
        scoreboard.setActor(actorList);

        instance = this;
    }

    /**
     * Constructor that only takes as parameters the gameMode and the number of players.
     * Every other parameter are taken as default
     */
    public GameBuilder(GameMode gameMode, int numOfPlayer) {
        this(gameMode, null, null, null, null, numOfPlayer);
    }

    /**
     * Constructor that only takes as parameters the number of players.
     * Every other parameter are taken as default (default gameMode is Normal mode)
     */
    public GameBuilder(int numOfPlayer) {
        this(GameMode.NORMAL, numOfPlayer);
    }

    /**
     * Constructor that takes every aspect of the game as parameter
     */
    public GameBuilder( String mapPath,
                        String weaponPath,
                        String powerUpPath,
                        String ammoCardPath,
                        int numOfPlayer)
    {
        this(GameMode.NORMAL, mapPath, weaponPath, powerUpPath, ammoCardPath, numOfPlayer);
    }


    private GameMap parserMap(GameMode gameMode, String mapPath, int numOfPlayer, Tuple3<Deck<Weapon>, Deck<AmmoCard>, Deck<PowerUp>> decks) {
        mapName = null;
        if (mapPath != null) {
            mapName = mapPath;
            return GameMap.gameMapFactory(gameMode, mapPath, numOfPlayer, decks);
        }

        if (numOfPlayer == 3)
            mapName = "map1";
        if (numOfPlayer == 4)
            mapName = "map2";
        if (numOfPlayer == 5)
            mapName = "map3";

        if (mapName != null)
            return GameMap.gameMapFactory(gameMode, ParserConfiguration.parsePath(mapName + "Path"), numOfPlayer, decks);

        mapName = "map1";
        return GameMap.gameMapFactory(gameMode, ParserConfiguration.parsePath(mapName + "Path"), numOfPlayer, decks);
    }

    private Deck<AmmoCard> parserAmmoTile(String ammoCardPath) {
        return ammoCardPath==null
                ? new Deck<>(ParserAmmoTile.parse(ParserConfiguration.parsePath("ammoTilePath")))
                : new Deck<>(ParserAmmoTile.parse(ammoCardPath));
    }

    private Deck<PowerUp> parserPowerUp(String powerUpPath) {
        return powerUpPath==null
                ? new Deck<>(ParserPowerUp.parse(ParserConfiguration.parsePath("powerUpPath")))
                : new Deck<>(ParserPowerUp.parse(powerUpPath));
    }

    private Deck<Weapon> parserWeapon(String weaponPath) {
        return weaponPath==null
                ? new Deck<>(ParserWeapon.parseWeapons(ParserConfiguration.parsePath("weaponPath")))
                : new Deck<>(ParserWeapon.parseWeapons(weaponPath));

    }

    private List<Actor> buildActor(GameMap map) {
        List<Actor> actors = new ArrayList<>();
        boolean firstPlayer = true;
        for(DamageableUID pawnID : map.getDamageable()) {
            if(!(map.getPawn(pawnID) instanceof DominationPoint)) {
                Actor actor = new Actor(map, pawnID, firstPlayer);
                actor.setBinding();
                actors.add(actor);
                firstPlayer = false;
            }
            else {
                DominationPointTile tile = ((DominationPoint) map.getPawn(pawnID)).getDominationPointTile();
                tile.getControlPointActor().setBinding();

                tile.addTrack(scoreboard);
                dominationPointActor.put(
                        map.getCoord(((DominationPoint)map.getPawn(pawnID)).getDominationPointTile().getTileID()),
                        ((DominationPoint)map.getPawn(pawnID)).getDominationPointTile().getControlPointActor()
                );
            }
        }
        return actors;
    }


    public Deck<Weapon> getDeckOfWeapon(){
        return deckOfWeapon;
    }
    public Deck<PowerUp> getDeckOfPowerUp(){
        return deckOfPowerUp;
    }
    public Deck<AmmoCard> getDeckOfAmmoCard(){
        return deckOfAmmoCard;
    }
    public GameMap getMap(){
        return map;
    }
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
    public List<Actor> getActorList(){
        return new ArrayList<>(actorList);
    }
    public String getMapName() {
        return mapName;
    }
    public GameMode getGameMode() {
        return gameMode;
    }
    public Map<Coord, Actor> getDominationPointActor() {
        return dominationPointActor;
    }
}

