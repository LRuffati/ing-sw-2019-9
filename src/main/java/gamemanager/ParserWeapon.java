package gamemanager;

import actions.Action;
import actions.ActionInfo;
import actions.ActionTemplate;
import actions.conditions.*;
import actions.effects.*;
import actions.selectors.*;
import actions.targeters.TargeterTemplate;
import actions.utils.AmmoAmount;
import actions.utils.AmmoColor;
import genericitems.Tuple;
import grabbables.Weapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserWeapon {
    ParserWeapon(){}

    public static Collection<Weapon> parse(String path) throws FileNotFoundException{
        parseWeapons(path);
        Collection<Weapon> weaponCollection = new ArrayList<>();
        Scanner scanner;
        Scanner sLine;
        String name = null;
        AmmoAmount buyWeapon = null;
        AmmoAmount reloadWeapon = null;
        Collection<ActionTemplate> actions = new ArrayList<>();
        String mainAction = null;
        int countWeap = 0;



        scanner = new Scanner(ClassLoader.getSystemResourceAsStream(path));

        while(scanner.hasNextLine()){
            String weaponId = null;
            String description;
            String scannerString = scanner.nextLine();
            if(scannerString.equals("")) scannerString = scanner.nextLine();
            sLine = new Scanner(scannerString);
            sLine.useDelimiter(" ");
            Map<AmmoColor, Integer> amountGiven = new HashMap<>();
            String toBegin = sLine.next();
            int B = 0;
            int Y = 0;
            int R = 0;
            switch(toBegin) {
                case "weapon":
                    countWeap +=1;
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
                    reloadWeapon = new AmmoAmount(amountGiven);

                    switch (ammoColour.charAt(0)) {
                        case 'B':
                            amountGiven.replace(AmmoColor.BLUE, B - 1);
                            break;
                        case 'R':
                            amountGiven.replace(AmmoColor.RED, R - 1);
                            break;
                        case 'Y':
                            amountGiven.replace(AmmoColor.YELLOW, Y - 1);
                            break;
                        default:
                            break;
                    }

                    buyWeapon = new AmmoAmount(amountGiven);
                    //System.out.println("\n" + "Costo arma: " + buyWeapon.toString() + "\n" + "Ricarica arma: " + reloadWeapon.toString() + "\n");
                    break;

                case "nomeWeapon:":
                    name = scannerString.substring(scannerString.indexOf(':')+1).trim();
                    break;

                case "descrizioneWeapon:":
                    description = scannerString.substring(scannerString.indexOf(':'));
                    break;

                case "action":
                    amountGiven = new HashMap<>();
                    AmmoAmount actionPrice = new AmmoAmount();
                    String actionId = sLine.next();
                    String maybeCost = null;
                    boolean mNotNull = false;
                    if(sLine.hasNext()) {
                        maybeCost = sLine.next();
                        mNotNull = true;
                    }
                    if (mNotNull && maybeCost.matches("^[RBY]+$")) {
                        for (int i = 0; i < maybeCost.length() - 1; i++) {
                            switch (maybeCost.charAt(i)) {
                                case 'B':
                                    B += 1;
                                    break;
                                case 'R':
                                    R += 1;
                                    break;
                                case 'Y':
                                    Y += 1;
                                    break;
                                default:
                                    break;
                            }
                        }
                        amountGiven.put(AmmoColor.BLUE, B);
                        amountGiven.put(AmmoColor.RED, R);
                        amountGiven.put(AmmoColor.YELLOW, Y);
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
                    Collection<Tuple<Boolean, String>> actionRequirements = new ArrayList<>();
                    Collection<Tuple<Boolean, String>> targetRequirements = new ArrayList<>();
                    if(actionId.charAt(actionId.length()-1)==':') {
                        actionId = actionId.substring(0, actionId.length() - 1);
                    }
                    if (sLine.hasNext() && (sLine.next().equals("follows")||maybeCost.equals("follows"))){
                        listaFollow = sLine.next();
                        String substring = listaFollow
                                .substring(2, listaFollow.length() - 2);
                        if(listaFollow.charAt(1)=='!') {
                            actionRequirements.add(new Tuple<>(false, substring));
                        } else {
                            actionRequirements.add(new Tuple<>(true, substring));
                        }

                    }
                    if (sLine.hasNext() && (sLine.next().equals("exist")|| maybeCost.equals("exist"))) {
                        listaTarget = sLine.next();
                        String substring = listaTarget
                                .substring(2, listaTarget.length() - 2);
                        if(listaTarget.charAt(1)=='!') {
                            targetRequirements.add(new Tuple<>(false, substring));
                        } else {
                            targetRequirements.add(new Tuple<>(true, substring));
                        }
                    }
                    if (sLine.hasNext() && (sLine.next().equals("xor")||maybeCost.equals("xor"))) {
                        listaAZ = sLine.next().substring(1);
                        while(listaAZ.charAt(listaAZ.length()-1)==','){
                            actionRequirements.add(new Tuple<>(false, listaAZ.replace(listaAZ.substring(listaAZ.length()-1),"")));
                            listaAZ = sLine.next();
                        }
                        actionRequirements.add(new Tuple<>(false, listaAZ.replace(listaAZ.substring(listaAZ.length()-1),"")));
                    }
                    if (sLine.hasNext() && (sLine.next().equals("contemp")|| maybeCost.equals("contemp"))) {
                        idAction = sLine.next();
                        if(mainAction==null)
                            mainAction = idAction.substring(0,idAction.length()-2);

                    }

                    String toIf = scanner.nextLine();

                    if (toIf.contains("nomeAction:")) {
                        actionName = toIf.substring(toIf.indexOf(':')+1).trim();
                        toIf = scanner.nextLine();
                    }
                    if (toIf.equals("descrizioneAction:")) {
                        actionDescription = toIf.substring(toIf.indexOf(':')+1).trim();
                    }

                    List<Tuple<String, TargeterTemplate>> targeters = new ArrayList<>();
                    List<EffectTemplate> effects = new ArrayList<>();
                    sLine = new Scanner(scanner.nextLine());

                    if(sLine.next().equals("target")){
                        targetId = sLine.next();
                        targetType = sLine.next();
                        List<Tuple<String, Condition>> filters = new ArrayList<>();
                        selector = sLine.next().substring(1);
                        Selector toSelector = null;
                        int min = 123123123;
                        int max = 321321321;
                        Pattern p;
                        Matcher m;
                        switch(selector.toLowerCase()){

                            case "reached":
                                range = sLine.next();
                                p = Pattern.compile("\\d+");
                                m = p.matcher(range);
                                if(m.find()) min = Integer.parseInt(m.group());
                                if(m.find()) max = Integer.parseInt(m.group());
                                toSelector = new ReachableSelector(min,max);
                                toTargetId = sLine.next();
                                break;

                            case "distant":
                                range = sLine.next();
                                p = Pattern.compile("\\d+");
                                m = p.matcher(range);
                                if(m.find()) min = Integer.parseInt(m.group());
                                if(m.find()) max = Integer.parseInt(m.group());
                                toSelector = new DistanceSelector(min,max,true);
                                toTargetId = sLine.next();
                                break;

                            case "in":
                                toSelector = new ContainedSelector();
                                toTargetId = sLine.next();
                                break;

                            case "has":
                                toSelector = new HasSelector();
                                toTargetId = sLine.next();
                                break;

                            case "seen":
                                toSelector = new VisibleSelector();
                                toTargetId = sLine.next();
                                break;

                            case "exists":
                                toSelector = new ExistSelector();
                                toTargetId = "self";
                                break;

                            default:
                                break;
                        }

                        while(sLine.hasNext() && sLine.next().equals("&")){
                            String condition = sLine.next();
                            Condition toCondition = null;
                            String idTarg = null;
                            boolean not = false;
                            if(condition.equals("not")){
                                not = true;
                                condition = sLine.next();
                            }

                            switch(condition.toLowerCase()){
                                case "distant":
                                    range = sLine.next();
                                    p = Pattern.compile("\\d+");
                                    m = p.matcher(range);
                                    if(m.find()) min = Integer.parseInt(m.group());
                                    if(m.find()) max = Integer.parseInt(m.group());

                                    idTarg = sLine.next();
                                    toCondition = new DistantCondition(min,max,true, not);
                                    break;

                                case "in":
                                    idTarg = sLine.next();
                                    toCondition = new InCondition(not);
                                    break;

                                case "has":
                                    idTarg = sLine.next();
                                    toCondition = new HasCondition(not);
                                    break;

                                case "reaches":
                                    range = sLine.next();
                                    p = Pattern.compile("\\d+");
                                    m = p.matcher(range);
                                    if(m.find()) min = Integer.parseInt(m.group());
                                    if(m.find()) max = Integer.parseInt(m.group());
                                    idTarg = sLine.next();
                                    toCondition = new ReachesCondition(min, max, not);
                                    break;

                                case "seen":
                                    idTarg = sLine.next();
                                    toCondition = new SeenCondition(not);
                                    break;

                                default:
                                    break;
                            }
                            filters.add(new Tuple<>(idTarg,toCondition));
                        }

                        if(sLine.hasNext() && sLine.next().equals("new")) ifNew = true;
                        if(sLine.hasNext() && sLine.next().equals("automatic")) ifAutomatic = true;
                        if(sLine.hasNext() && sLine.next().equals("optional")) ifOptional = true;

                        targeters.add(new Tuple<>(targetId,new TargeterTemplate(new Tuple<>(toTargetId,toSelector),
                                filters,targetType,ifOptional,ifNew,ifAutomatic)));

                    }

                    String effectIf = scanner.nextLine();
                    sLine = new Scanner(effectIf);
                    if (effectIf.contains("effect")) {
                        sLine.next();
                        String effect = sLine.next();
                        EffectTemplate toEffect = null;
                        int amount;
                        String targId;
                        String targIdbis;

                        switch (effect.toLowerCase()) {
                            case "fire":
                                toEffect = new Fire();
                                break;

                            case "damage":
                                amount = Integer.parseInt(sLine.next());
                                targId = sLine.next();
                                toEffect = new DamageTemplate(targId, amount);
                                break;

                            case "reload":
                                toEffect = new ReloadTemplate();
                                break;

                            case "grab":
                                toEffect = new GrabTemplate();
                                break;

                            case "mark":
                                amount = Integer.parseInt(sLine.next());
                                targId = sLine.next();
                                toEffect = new MarkTemplate(targId, amount);
                                break;

                            case "move":
                                targId = sLine.next();
                                sLine.next();
                                targIdbis = sLine.next();
                                toEffect = new MoveTemplate(targId, targIdbis);
                                break;

                            case "pay":
                                int red = 0;
                                int yellow = 0;
                                int blue = 0;
                                int toAdd = 0;
                                Map<AmmoColor, Integer> givenA = new HashMap<>();

                                while(sLine.hasNext()) {
                                    toAdd = Integer.parseInt(sLine.next());
                                    switch (sLine.next()) {
                                        case "R,":
                                            red += toAdd;
                                            break;

                                        case "Y,":
                                            yellow += toAdd;
                                            break;

                                        case "B,":
                                            blue += toAdd;
                                            break;

                                        default:
                                            break;
                                    }
                                }
                                givenA.put(AmmoColor.RED, red);
                                givenA.put(AmmoColor.BLUE, blue);
                                givenA.put(AmmoColor.YELLOW, yellow);
                                AmmoAmount toPay = new AmmoAmount(givenA);
                                toEffect = new PayTemplate(toPay);
                                break;


                            default:
                                break;
                        }
                        effects.add(toEffect);
                    }
                    mainAction=null;
                    actions.add(new ActionTemplate(new ActionInfo(actionName, actionId,actionPrice,actionRequirements,
                            targetRequirements, Optional.ofNullable(mainAction),true),targeters,effects));
                    effects.clear();
                    break;

                case "stop":
                    weaponCollection.add(new Weapon(name,buyWeapon,reloadWeapon,actions));
                    actions.clear();
                    break;

                default:
                    break;
            }
            sLine.close();
        }
        scanner.close();
        return weaponCollection;
    }

    public static Set<Weapon> parseWeapons(String path) throws FileNotFoundException {

        Set<Weapon> weapons = new HashSet<>();
        Scanner scanner = new Scanner( ClassLoader.getSystemResourceAsStream(path));
        String wholeFile = scanner.useDelimiter("\\A").next();
        scanner.close();


        Matcher wholeFileMatcher = Pattern.compile("weapon +(\\w+) +([RBY]([RYB]*)) *:([\\w\\W]+?)\\nstop").matcher(wholeFile);

        while (wholeFileMatcher.find()) {
            Matcher wIdMatcher = Pattern.compile("weapon +(\\w+)").matcher(wholeFile);
            String weaponId = wIdMatcher.group().split(" ")[1];

            Matcher costMatcher = Pattern.compile("([RBY]([RYB]*))").matcher(wholeFile);
            String fullCost = costMatcher.group();
            int R = 0;
            int B = 0;
            int Y = 0;
            for (int i = 0; i < fullCost.length(); i++) {
                switch (fullCost.charAt(i)) {
                    case ('R'):
                        R++;
                        break;

                    case ('B'):
                        B++;
                        break;

                    case ('Y'):
                        Y++;
                        break;

                    default:
                        break;
                }
            }

            Map<AmmoColor, Integer> reloadCost = new HashMap<>();
            reloadCost.put(AmmoColor.RED, R);
            reloadCost.put(AmmoColor.BLUE, B);
            reloadCost.put(AmmoColor.YELLOW, Y);

            Map<AmmoColor, Integer> buyCost = new HashMap<>();
            if (R > 0) {
                buyCost.put(AmmoColor.RED, R - 1);
            } else buyCost.put(AmmoColor.RED, R);
            if (B > 0) {
                buyCost.put(AmmoColor.BLUE, B - 1);
            } else buyCost.put(AmmoColor.BLUE, B);
            if (Y > 0) {
                buyCost.put(AmmoColor.YELLOW, Y - 1);
            } else buyCost.put(AmmoColor.YELLOW, Y);

            Matcher bodyMatcher = Pattern.compile("([\\w\\W]+?)\\nstop").matcher(wholeFile);
            String body = bodyMatcher.group();

            weapons.add(parseSingleWeapon(weaponId, new AmmoAmount(reloadCost), new AmmoAmount(buyCost), body));

        }

        // regex:= "weapon +(\w+) +([RBY]([RYB]*)) *:([\w\W]+?)\nstop"
        // Per ogni match:
        //          Gruppo 1: weaponId
        //          2: Ricarica
        //          3: BuyCost
        //          4: body
        return weapons;
    }

    private static AmmoAmount parseAmmo(String ammoamount){



        return null;
    }

    private static Weapon parseSingleWeapon(String weaponId, AmmoAmount reloadCost, AmmoAmount buyCost, String body){
        // regex1:= nomeWeapon: +([ \w]+?) *\ndescrizioneWeapon: +([ \w]+?) *\n(action[\w\W]+)$
        // gruppo 1: nome
        // gruppo 2: descrizione
        // gruppo 3: actions
        //
        // regex2:= action +(\w+)(?: +([RYB]*))?(?: +follows +\[(.+?)\])?(?: +exist +\[(.+?)\])?(?: +xor +\[(.+?)\])?(?: +contemp +(\w+))? *:\n([\w\W]+?)(?=action|$)
        // 1: idAzione
        // 2: costo
        // 3: listaFollows ex "!main ciao"
        // 4: listaexist ^
        // 5: listaxor "act1 act2"
        // 6: contemp "main"
        // 7: body
        //
        // Per ogni match di regex2 chiama prima parseInfo e poi parseTarget
        // Dopo aver raccolto tutte le azioni verificare per i contemp
        return null;
    }

    private static ActionTemplate parseAction(ActionInfo info,
                                              String body){
        return null;
    }

    private static Tuple<String, TargeterTemplate> parseTarget(String line){
        return null;
    }

    private static EffectTemplate parseEffect(String body){
        return null;
    }

    private static ActionInfo parseInfo(String nomeId,
                                        String nome,
                                        String descrizione,
                                        AmmoAmount cost,
                                        String followsList,
                                        String existList,
                                        String xorList,
                                        String contemp){
        return null;
    }
}
