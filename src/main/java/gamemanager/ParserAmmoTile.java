package gamemanager;

import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import grabbables.AmmoCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

public class  ParserAmmoTile {
    ParserAmmoTile(){}

    public static Collection<AmmoCard> parse(String path) throws FileNotFoundException {

        Collection<AmmoCard> ammoCardCollection = new ArrayList<>();
        int numOfPowerUp;
        int numOfRed;
        int numOfYellow;
        int numOfBlue;


        Scanner scanner;
        Scanner sLine;

        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File AmmoTile not found");
        }

        while (scanner.hasNextLine()){
            numOfPowerUp = 0;
            numOfRed = 0;
            numOfBlue = 0;
            numOfYellow = 0;

            sLine = new Scanner(scanner.nextLine());
            sLine.useDelimiter("-");

            while (sLine.hasNext()) {
                switch (sLine.next()) {
                    case "RED":
                        numOfRed++;
                        break;
                    case "BLUE":
                        numOfBlue++;
                        break;
                    case "YELLOW":
                        numOfYellow++;
                        break;
                    case "POWERUP":
                        numOfPowerUp++;
                        break;
                    default:
                        break;
                }
            }
            sLine.close();

            if(numOfBlue+numOfRed+numOfYellow+numOfPowerUp == 0) continue;

            ammoCardCollection.add(new AmmoCard(
                    new AmmoAmount(Map.of(  AmmoColor.RED,numOfRed,
                                            AmmoColor.BLUE,numOfBlue,
                                            AmmoColor.YELLOW,numOfYellow)),
                    numOfPowerUp)
            );
        }
        scanner.close();
        return ammoCardCollection;
    }
}
