package gamemanager;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import grabbables.PowerUp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class ParserPowerUp {
    ParserPowerUp() {}

    public static Collection<PowerUp> parse(String path) throws FileNotFoundException {
        Collection<PowerUp> powerUpCollection = new ArrayList<>();

        Scanner scanner;
        Scanner sLine;
        PowerUpType type = null;
        AmmoColor col = null;

        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File PowerUp not found");
        }

        while (scanner.hasNextLine()){

            String str = scanner.nextLine();
            sLine = new Scanner(str);
            sLine.useDelimiter("-");


            if(sLine.hasNext()){
                type = getType(sLine.next());
                col = getCol(sLine.next());
                if(type != null && col != null){
                    powerUpCollection.add(new PowerUp(type, new AmmoAmount(Map.of(col,1))));
                }

            }
            sLine.close();
        }
        scanner.close();

        return powerUpCollection;
    }



    private static PowerUpType getType(String str){
        PowerUpType type;
        switch (str) {
            case "NEWTON":
                type = PowerUpType.NEWTON;
                break;
            case "TELEPORTER":
                type = PowerUpType.TELEPORTER;
                break;
            case "TAGBACK_GRENADE":
                type = PowerUpType.TAGBACKGRANADE;
                break;
            case "TARGETING_SCOPE":
                type = PowerUpType.TARGETINGSCOPE;
                break;

            default:
                type = null;
                break;
        }
        return type;
    }

    private static AmmoColor getCol(String str){
        AmmoColor col;
        switch (str){
            case "BLUE":
                col = AmmoColor.BLUE;
                break;
            case "RED":
                col = AmmoColor.RED;
                break;
            case "YELLOW":
                col = AmmoColor.YELLOW;
                break;

                default:
                    col = null;
                    break;
        }
        return col;
    }
}
