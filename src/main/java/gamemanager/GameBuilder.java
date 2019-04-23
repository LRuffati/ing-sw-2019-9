package gamemanager;

import board.GameMap;
import genericitems.Tuple3;
import grabbables.*;
import player.Actor;
import uid.DamageableUID;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private Deck<Weapon> deckOfWeapon;
    private Deck<PowerUp> deckOfPowerUp;
    private Deck<AmmoCard> deckOfAmmoCard;
    private List<Actor> actorList;
    private GameMap map;

    public GameBuilder(String mapPath,
                       String weaponPath,
                       String powerUpPath,
                       String ammoCardPath,
                       int numOfPlayer)
            throws FileNotFoundException{

        deckOfWeapon = parserWeapon(weaponPath);
        deckOfPowerUp = parserPowerUp(powerUpPath);
        deckOfAmmoCard = parserAmmoTile(ammoCardPath);

        Tuple3<Deck<Weapon>, Deck<AmmoCard>, Deck<PowerUp>>
                decks = new Tuple3<>(deckOfWeapon, deckOfAmmoCard, deckOfPowerUp);

        map = GameMap.gameMapFactory(mapPath, numOfPlayer, decks);

        actorList = buildActor(map);
    }

    Deck<Weapon> getDeckOfWeapon(){
        return deckOfWeapon;
    }
    Deck<PowerUp> getDeckOfPowerUp(){
        return deckOfPowerUp;
    }
    Deck<AmmoCard> getDeckOfAmmoCard(){
        return deckOfAmmoCard;
    }


    private Deck<AmmoCard> parserAmmoTile(String ammoCardPath) throws FileNotFoundException {
        return new Deck<>(ParserAmmoTile.parse(ammoCardPath));
    }

    private Deck<PowerUp> parserPowerUp(String powerUpPath) {
        return null;
    }

    private Deck<Weapon> parserWeapon(String weaponPath) {
        return null;
    }

    private List<Actor> buildActor(GameMap map) {
        List<Actor> actorList = new ArrayList<>();
        boolean firstPlayer = true;
        for(DamageableUID pawnID : map.getDamageable()){
            System.out.println(map.getPawn(pawnID));
            Actor actor = new Actor(map, pawnID, firstPlayer);
            actor.setBinding();
            actorList.add(actor);
            firstPlayer = false;
        }
        return actorList;
    }

}

