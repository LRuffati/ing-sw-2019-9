package gamemanager;

import actions.Action;
import actions.ActionInfo;
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
            String weaponId = null;
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
                weaponId = sLine.next();
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
                        default:
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
                    default:
                        break;
                }
                reloadWeapon = new AmmoAmount(amountGiven);
            }
            amountGiven = new HashMap<>();
            AmmoAmount actionPrice = null;
            if(sLine.next().equals("nome:")) name = sLine.next();
            if(sLine.next().equals("description:")) description = sLine.next();
            while(!sLine.next().equals("weapon")){
                if(sLine.next().equals("action")){
                    String actionId = sLine.next();
                    String maybeCost = sLine.next();
                    int B = 0;
                    int Y = 0;
                    int R = 0;
                    if(maybeCost.matches("^[RBY]+$")){
                        for(int i = 0; i < maybeCost.length()-1; i++){
                            switch(maybeCost.charAt(i)){
                                case 'B':
                                    B+=1;
                                    break;
                                case 'R':
                                    R+=1;
                                    break;
                                case 'Y':
                                    Y+=1;
                                    break;
                                default:
                                    break;
                            }
                        }
                        amountGiven.put(AmmoColor.BLUE,B);
                        amountGiven.put(AmmoColor.RED,R);
                        amountGiven.put(AmmoColor.YELLOW,Y);
                        actionPrice = new AmmoAmount(amountGiven);
                    }


                    String listaFollow = null;
                    String listaTarget = null;
                    String listaAZ = null;
                    String idAction = null;
                    String actionName = null;
                    String actionDescription = null;
                    String targetId = null;
                    String targetType = null;
                    String selector = null;
                    String toTargetId = null;
                    String range = null;
                    boolean ifNew = false;
                    boolean ifAutomatic = false;
                    boolean ifOptional = false;
                    actionId = actionId.substring(0, actionId.length() - 1);
                    if (sLine.next().equals("follows")||maybeCost.equals("follows")) listaFollow = sLine.next();
                    if (sLine.next().equals("exist")||maybeCost.equals("exist")) listaTarget = sLine.next();
                    if (sLine.next().equals("xor")||maybeCost.equals("xor")) listaAZ = sLine.next();
                    if (sLine.next().equals("contemp")||maybeCost.equals("contemp")) idAction = sLine.next();
                    if (sLine.next().equals("nome:")||maybeCost.equals("nome:")) actionName = sLine.nextLine();
                    if (sLine.next().equals("descrizione:")) actionDescription = sLine.nextLine();
                    while(!sLine.next().equals("action")&&!sLine.next().equals("effect")){
                        if(sLine.next().equals("target")){
                            targetId = sLine.next();
                            targetType = sLine.next();
                            selector = sLine.next().substring(1);
                            switch(selector.toLowerCase()){

                                case "reached":

                                case "distant":
                                    range = sLine.next();
                                    toTargetId = sLine.next();
                                    break;

                                case "in":

                                case "has":

                                case "seen":
                                    toTargetId = sLine.next();
                                    break;

                                default:
                                    break;
                            }

                            if(sLine.next().equals("new")) ifNew = true;
                            if(sLine.next().equals("automatic")) ifAutomatic = true;
                            if(sLine.next().equals("optional")) ifOptional = true;
                        }
                    }
                    //actions.add(new ActionTemplate(new ActionInfo(actionName, actionId,actionPrice,)));
                }
            }
            weaponCollection.add(new Weapon(name,buyWeapon,reloadWeapon,actions));
            sLine.close();
        }

        scanner.close();
        return weaponCollection;
    }
}
