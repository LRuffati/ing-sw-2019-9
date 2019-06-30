package gamemanager;

import actions.utils.AmmoColor;
import actions.utils.PowerUpType;
import grabbables.PowerUp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Class used to parse a file containing PowerUp cards.
 */
public class ParserPowerUp {
    ParserPowerUp() {}

    /**
     * Static method that parses the file
     * @param path the Path of the file that has to be parsed
     * @return A collection containing all the Card parsed
     */
    public static Collection<PowerUp> parse(String path) {
        Collection<PowerUp> powerUpCollection = new ArrayList<>();

        Scanner scanner;
        Scanner sLine;
        PowerUpType type = null;
        AmmoColor col = null;

        scanner = new Scanner(ClassLoader.getSystemResourceAsStream(path));
        /*
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File PowerUp not found");
        }
        */

        while (scanner.hasNextLine()){

            String str = scanner.nextLine();
            sLine = new Scanner(str);
            sLine.useDelimiter("-");


            if(sLine.hasNext()){
                type = getType(sLine.next());
                col = getCol(sLine.next());
                if(type != null && col != null){
                    powerUpCollection.add(PowerUp.powerUpFactory(type, col));
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
