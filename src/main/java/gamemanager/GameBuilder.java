package gamemanager;

import grabbables.*;

import java.io.FileNotFoundException;

public class GameBuilder {
    private Deck<Weapon> deckOfWeapon;
    private Deck<PowerUp> deckOfPowerUp;
    private Deck<AmmoCard> deckOfAmmoCard;

    public GameBuilder(String mapPath, String weaponPath, String powerUpPath, String ammoCardPath) throws FileNotFoundException{
        deckOfWeapon = parserWeapon(weaponPath);
        deckOfPowerUp = parserPowerUp(powerUpPath);
        deckOfAmmoCard = parserAmmoTile(ammoCardPath);
    }

    protected Deck<Weapon> getDeckOfWeapon(){
        return deckOfWeapon;
    }
    protected Deck<PowerUp> getDeckOfPowerUp(){
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

}

