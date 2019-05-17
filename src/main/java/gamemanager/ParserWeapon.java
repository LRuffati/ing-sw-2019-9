package gamemanager;

import actions.ActionTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import grabbables.Weapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ParserWeapon {
    ParserWeapon(){}

    public static Collection<Weapon> parse(String path) throws FileNotFoundException{
        Collection<Weapon> weaponCollection = new ArrayList<>();
        Scanner scanner;
        Scanner sLine;

        try{
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File Weapons not found");
        }

        while(scanner.hasNextLine()){
            String name = null;
            String description;
            Collection<ActionTemplate> actions = new ArrayList<>();
            sLine = new Scanner(scanner.nextLine());
            sLine.useDelimiter(" ");
            Map<AmmoColor, Integer> amountGiven = new HashMap<>();
            if(sLine.next().equals("weapon")) {
                int B = 0;
                int Y = 0;
                int R = 0;
                name = sLine.next();
                String ammoColour = sLine.next();
                for(int i = 0; i < ammoColour.length()-1; i++){
                    switch(ammoColour.charAt(0)){
                        case 'B':
                            B+=1;
                            break;
                        case 'R':
                            R+=1;
                            break;
                        case 'Y':
                            Y+=1;
                            break;
                    }
                }
                amountGiven.put(AmmoColor.BLUE,B);
                amountGiven.put(AmmoColor.RED,R);
                amountGiven.put(AmmoColor.YELLOW,Y);
            }
            AmmoAmount buyWeapon = new AmmoAmount(amountGiven);
            if(sLine.next().equals("nome:")) name = sLine.next();
            if(sLine.next().equals("description:")) description = sLine.next();
            if(sLine.next().equals("action")){

            }
            weaponCollection.add(new Weapon(name,buyWeapon,buyWeapon,actions));
            sLine.close();
        }

        scanner.close();
        return weaponCollection;
    }
}
