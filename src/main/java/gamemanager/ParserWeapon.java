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
            AmmoAmount buyWeapon = null;
            AmmoAmount reloadWeapon = null;
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
                    switch(ammoColour.charAt(i)){
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
                buyWeapon = new AmmoAmount(amountGiven);
                switch(ammoColour.charAt(0)){
                    case 'B':
                        amountGiven.replace(AmmoColor.BLUE, B-1);
                        break;
                    case 'R':
                        amountGiven.replace(AmmoColor.RED, R-1);
                        break;
                    case 'Y':
                        amountGiven.replace(AmmoColor.YELLOW, Y-1);
                        break;
                }
                reloadWeapon = new AmmoAmount(amountGiven);
            }

            if(sLine.next().equals("nome:")) name = sLine.next();
            if(sLine.next().equals("description:")) description = sLine.next();
            while(!sLine.next().equals("weapon")){
                if(sLine.next().equals("action")){
                    String actionId = sLine.next();
                    String actionName = null;
                    String actionDescription = null;
                    actionId = actionId.substring(0, actionId.length() - 1);
                    if (sLine.next().equals("nome:")) actionName = sLine.nextLine();
                    if (sLine.next().equals("descrizione:")) actionDescription = sLine.nextLine();
                    while(!sLine.next().equals("action")){
                        if(sLine.next().equals("target")){

                        }
                    }
                }
            }
            weaponCollection.add(new Weapon(name,buyWeapon,reloadWeapon,actions));
            sLine.close();
        }

        scanner.close();
        return weaponCollection;
    }
}
