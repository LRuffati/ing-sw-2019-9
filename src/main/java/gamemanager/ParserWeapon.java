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

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

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

        Scanner scanner = new Scanner( ClassLoader.getSystemResourceAsStream(path));
        String wholeFile = scanner.useDelimiter("\\A").next();
        scanner.close();


        Matcher wholeFileMatcher = Pattern.compile("weapon +(\\w+) +([RBY]([RYB]*)) *:\\n" +
                "([\\w\\W]+?)\\nstop").matcher(wholeFile);

        return wholeFileMatcher.results()
                .map(m -> {
                    String weaponId = m.group(1);
                    AmmoAmount reloadCost = parseAmmo(m.group(2));
                    AmmoAmount buyCost = parseAmmo(m.group(3));
                    return parseSingleWeapon(weaponId,reloadCost,buyCost,m.group(4));
                }).collect(Collectors.toSet());

        // regex:= "weapon +(\w+) +([RBY]([RYB]*)) *:([\w\W]+?)\nstop"
        // Per ogni match:
        //          Gruppo 1: weaponId
        //          2: Ricarica
        //          3: BuyCost
        //          4: body
    }

    private static AmmoAmount parseAmmo(String ammoamount){
        if(ammoamount.length()==0){
            return new AmmoAmount();
        } else {
            int red = (int) ammoamount.chars().filter(ch -> ch == 'R').count();
            int blue = (int) ammoamount.chars().filter(ch -> ch == 'B').count();
            int yellow = (int) ammoamount.chars().filter(ch -> ch == 'Y').count();

            Map<AmmoColor, Integer> amount = new HashMap<>();
            amount.put(AmmoColor.YELLOW, yellow);
            amount.put(AmmoColor.BLUE, blue);
            amount.put(AmmoColor.RED, red);
            return new AmmoAmount(amount);
        }
    }

    private static Weapon parseSingleWeapon(String weaponId, AmmoAmount reloadCost, AmmoAmount buyCost, String body){

        // regex1:= nomeWeapon: +([ \w]+?) *\ndescrizioneWeapon: +([ \w]+?) *\n(action[\w\W]+)$
        // gruppo 1: nome
        // gruppo 2: descrizione
        // gruppo 3: actions
        //

        Matcher header = Pattern.compile("nomeWeapon: +([ \\w]+?) *\\ndescrizioneWeapon: +([ \\w]+?) *\\n").matcher(body);
        header.find();
        String nome = header.group(1);
        String descrizione = header.group(2);

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

        String regexAction = "action +(\\w+)(?: +([RYB]*))?(?: +follows +\\[ *(.+?) *\\])?(?: " +
                "+exist " +
                "+\\[ *(.+?) *\\])?(?: +xor +\\[ *(.+?) *\\])?(?: +contemp +(\\w+))? *:\n" +
                "([\\w\\W]+?)" +
                "(?=\\naction|$)";

        Matcher actionsBody = Pattern.compile(regexAction).matcher(body);

        List<ActionTemplate> actions = actionsBody.results()
                .map(match -> {
                    String id = match.group(1);

                    String costString = match.group(2);
                    if (costString==null)
                        costString="";

                    String follows = match.group(3);
                    if (follows==null)
                        follows="";

                    String exists = match.group(4);
                    if (exists==null)
                        exists="";

                    String xor = match.group(5);
                    if (xor==null)
                        xor="";

                    //TODO probably to change
                    String contemp = match.group(6);
                    if (contemp==null)
                        contemp="";

                    String bodyAction = match.group(7);

                    AmmoAmount cost = parseAmmo(costString);

                    Matcher followMatcher = Pattern.compile("(!)?(\\w+)").matcher(follows);
                    List<Tuple<Boolean, String>> followsList = followMatcher.results().map(m ->
                    {
                        return new Tuple<>(m.group(1)==null, m.group(2));
                    }).collect(Collectors.toList());

                    Matcher existsMatcher = Pattern.compile("(!)?(\\w+)").matcher(exists);
                    List<Tuple<Boolean, String>> existsList = existsMatcher.results().map(m ->
                    {
                        return new Tuple<>(m.group(1)==null, m.group(2));
                    }).collect(Collectors.toList());

                    Matcher xorMatcher = Pattern.compile("(\\w+)").matcher(xor);
                    List<String> xorList = xorMatcher.results().map(m ->
                    {
                        return m.group();
                    }).collect(Collectors.toList());

                    return parseAction(id,cost,followsList,existsList,xorList,contemp,bodyAction);
                })
                .collect(Collectors.toList());

        return new Weapon(nome,buyCost,reloadCost,actions);
    }


    private static ActionTemplate parseAction(String actionID,
                                              AmmoAmount cost,
                                              List<Tuple<Boolean, String>> follows,
                                              List<Tuple<Boolean, String>> targetsExist,
                                              List<String> xor,
                                              String contemp,
                                              String body){

        Matcher header = Pattern.compile("nomeAction: +([ \\w]+?) *\\ndescrizioneAction: +([ \\w]+?) *\\n").matcher(body);
        header.find();
        String actionName = header.group(1);
        String actionDes = header.group(2);

        ActionInfo actionInfo = parseInfo(actionID, actionName, actionDes, cost, follows, targetsExist,xor,contemp);

        Pattern matchTargetDef = Pattern.compile("target.+\\n");
        List<Tuple<String, TargeterTemplate>> targTemplates= matchTargetDef.matcher(body).results()
                .map(m->parseTarget(m.group(0)))
                .collect(Collectors.toList());

        Pattern matchEffectDef = Pattern.compile("effect.+\\n");
        List<EffectTemplate> effectTemplates = matchEffectDef.matcher(body).results()
                .map(m->parseEffect(m.group(0)))
                .collect(Collectors.toList());

        return new ActionTemplate(actionInfo, targTemplates, effectTemplates);
    }

    private static Tuple<String, TargeterTemplate> parseTarget(String line){
        Matcher targetMatcher = Pattern.compile("target +(\\w+) +(pawn|tile|room|direction|group) +\\( *(DISTANT|IN|EXISTS|IS|HAS|REACHED|SEEN)(?: +(.+?))? +(?=&|\\))(?:& +(.+?) *)?\\)( +new)?( +automatic)?( +optional)?\\n").matcher(line);
        targetMatcher.find();
        String targName = targetMatcher.group(1);
        String targType = targetMatcher.group(2);
        String selectorType = targetMatcher.group(3);
        String selectorParam = targetMatcher.group(4);
        if(selectorParam==null) selectorParam = "";

        Matcher selectorParamMatcher;
        Tuple<String, Selector> selector = new Tuple<>(null,null);

        switch (selectorType.toLowerCase()){
            case "in":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(),new ContainedSelector());
                break;
            case "is":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(),new ExistSelector());
                break;
            case "has":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(),new HasSelector());
                break;
            case "seen":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(),new VisibleSelector());
                break;

            case "distant":
                selectorParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(3),
                        new DistanceSelector(Integer.parseInt(selectorParamMatcher.group(1)), Integer.parseInt(selectorParamMatcher.group(2)), false));
                break;
            case "reached":
                selectorParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(3),
                        new ReachableSelector(Integer.parseInt(selectorParamMatcher.group(1)), Integer.parseInt(selectorParamMatcher.group(2))));
                break;

            default:
                break;
        }

        String conditions = targetMatcher.group(5);
        List<Tuple<String, Condition>> conditionsList = new ArrayList<>();
        if(conditions==null) {
            conditions = "";
        } else {
            Matcher conditionsMatcher = Pattern.compile("(NOT|not) +(DISTANT|HAS|IN|REACHES|SEEN) +(.+?) *(?:&|$)").matcher(conditions);


            conditionsList = conditionsMatcher.results().map(m -> {
                Matcher conditionParamMatcher;
                Condition condition;
                Tuple<String, Condition> toReturn = null;
                switch (m.group(2).toLowerCase()){
                    case "in":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new InCondition(m.group(1)==null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "has":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new HasCondition(m.group(1)==null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "seen":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new SeenCondition(m.group(1)==null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "distant":
                        conditionParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new DistantCondition(Integer.parseInt(conditionParamMatcher.group(1)), Integer.parseInt(conditionParamMatcher.group(2)), true,m.group(1)==null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(3),condition);
                        break;

                    case "reached":
                        conditionParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new ReachesCondition(Integer.parseInt(conditionParamMatcher.group(1)), Integer.parseInt(conditionParamMatcher.group(2)), m.group(1)==null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(3),condition);
                        break;

                    default:
                        break;

                }
                return toReturn;
            }).collect(Collectors.toList());




        }

        String isNew = targetMatcher.group(6);
        String isAuto = targetMatcher.group(7);
        String isOpt = targetMatcher.group(8);

        return new Tuple<>(targName, new TargeterTemplate(selector,conditionsList,targType,isOpt!=null,isNew!=null, isAuto!=null));
    }

    private static EffectTemplate parseEffect(String body){

        EffectTemplate effect = null;
        Matcher effectMatcher = Pattern.compile("effect +(FIRE|DAMAGE|RELOAD|GRAB|MARK|MOVE|PAY) *(.*)(?:$|\\n)").matcher(body);
        effectMatcher.find();

        Matcher effectParamMatcher;
        switch (effectMatcher.group(1).toLowerCase()){
            case "fire":
                effect = new Fire();
                break;

            case "damage":
                effectParamMatcher = Pattern.compile(" *([1-9]?\\d+) +(\\w+)").matcher(effectMatcher.group(2));
                effectParamMatcher.find();
                effect = new DamageTemplate(effectParamMatcher.group(2), Integer.parseInt(effectParamMatcher.group(1)));
                break;

            case "reload":
                effect = new ReloadTemplate();
                break;

            case "grab":
                effect = new GrabTemplate();
                break;

            case "mark":
                effectParamMatcher = Pattern.compile(" *([1-9]?\\d+) +(\\w+)").matcher(effectMatcher.group(2));
                effectParamMatcher.find();
                effect = new MarkTemplate(effectParamMatcher.group(2), Integer.parseInt(effectParamMatcher.group(1)));
                break;

            case "move":
                effectParamMatcher = Pattern.compile(" +(\\w+) to +(\\w+)").matcher(effectMatcher.group(2));
                effectParamMatcher.find();
                effect = new MoveTemplate(effectParamMatcher.group(1), effectParamMatcher.group(1));
                break;

            case "pay":
                effectParamMatcher = Pattern.compile("(\\d+ (R|Y|B), )+").matcher(effectMatcher.group(2));
                Map<AmmoColor, Integer> amountMap = new HashMap<>();
                List<String> sad = effectParamMatcher.results().map(m -> {
                    switch (m.group(2).toLowerCase()){
                        case "r":
                            amountMap.put(AmmoColor.RED, Integer.parseInt(m.group(1)));
                            break;

                        case "y":
                            amountMap.put(AmmoColor.YELLOW, Integer.parseInt(m.group(1)));
                            break;

                        case "b":
                            amountMap.put(AmmoColor.BLUE, Integer.parseInt(m.group(1)));
                            break;

                    }
                    return "asd";
                }).collect(Collectors.toList());
                effect = new PayTemplate(new AmmoAmount(amountMap));
                break;

            default:
                break;

        }

        return effect;
    }

    private static ActionInfo parseInfo(String nomeId,
                                        String nome,
                                        String descrizione,
                                        AmmoAmount cost,
                                        List<Tuple<Boolean, String>> followsList,
                                        List<Tuple<Boolean, String>> existList,
                                        List<String> xorList,
                                        String contemp){

        Collection<Tuple<Boolean, String>> actionRequirements = new ArrayList<>();
        Collection<Tuple<Boolean, String>> targetRequirements = new ArrayList<>();

        for(Tuple<Boolean,String> follow : followsList){
            actionRequirements.add(new Tuple<>(follow.x, follow.y));
        }

        for(Tuple<Boolean, String> exist : existList){
            targetRequirements.add(new Tuple<>(exist.x, exist.y));
        }

        for(String xor : xorList){
            actionRequirements.add(new Tuple<>(false,xor));
        }
        //TODO Contemp

        return new ActionInfo(nome, nomeId,cost,actionRequirements,targetRequirements,Optional.ofNullable(contemp),true);
    }

}
