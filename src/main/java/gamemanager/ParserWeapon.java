package gamemanager;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParserWeapon {
    private static String regexNomeDescr = "[\\/ \\w'-.,òèàùé]";
    private static String regexEndLine = System.getProperty("line.separator");
    ParserWeapon(){}

    public static List<Weapon> parseWeapons(String path) throws FileNotFoundException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        Scanner scanner = new Scanner(stream);
        String wholeFile = scanner.useDelimiter("\\A").next();
        scanner.close();


        Matcher wholeFileMatcher = Pattern.compile("weapon +(\\w+) +([RBY]([RYB]*)) *:"+regexEndLine+"" +
                "([\\w\\W]+?)"+regexEndLine+"stop").matcher(wholeFile);

        return wholeFileMatcher.results()
                .map(m -> {
                    String weaponId = m.group(1);
                    AmmoAmount reloadCost = parseAmmo(m.group(2));
                    AmmoAmount buyCost = parseAmmo(m.group(3));
                    return parseSingleWeapon(weaponId, reloadCost, buyCost, m.group(4));
                }).collect(Collectors.toList());

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

        Matcher header = Pattern.compile("nomeWeapon: +("+regexNomeDescr+"+?) " +
                "*"+regexEndLine+"descrizioneWeapon: +("+regexNomeDescr+"+?) *"+regexEndLine+"").matcher(body);
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
                "+\\[ *(.+?) *\\])?(?: +xor +\\[ *(.+?) *\\])?(?: +contemp +(\\w+))? *:"+regexEndLine +
                "([\\w\\W]+?)" +
                "(?="+regexEndLine+"action|$)";

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
                    followsList.add(new Tuple<>(Boolean.FALSE, id));

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

        Matcher header = Pattern.compile("nomeAction: +("+regexNomeDescr+"+?) *"+regexEndLine+"descrizioneAction: " +
                "+("+regexNomeDescr+"+?) *(?:"+regexEndLine+"|$)").matcher(body);
        header.find();
        String actionName = header.group(1);
        String actionDes = header.group(2);

        ActionInfo actionInfo = parseInfo(actionID, actionName, actionDes, cost, follows, targetsExist,xor,contemp);

        Pattern matchTargetDef = Pattern.compile(""+regexEndLine+"target.+(?="+regexEndLine+"|$)");
        List<Tuple<String, TargeterTemplate>> targTemplates= matchTargetDef.matcher(body).results()
                .map(m->parseTarget(m.group(0)))
                .collect(Collectors.toList());

        Pattern matchEffectDef = Pattern.compile("effect.+(?:"+regexEndLine+"|$)");
        List<EffectTemplate> effectTemplates = matchEffectDef.matcher(body).results()
                .map(m->parseEffect(m.group(0)))
                .collect(Collectors.toList());

        return new ActionTemplate(actionInfo, targTemplates, effectTemplates);
    }

    private static Tuple<String, TargeterTemplate> parseTarget(String line){
        Matcher targetMatcher = Pattern.compile(""+regexEndLine+"target +(\\w+) +" +
                "(pawn|tile|room|direction|group|directionph) +\\( *" +
                "(DISTANTPH|DISTANT|IN|EXISTS|IS|HAS|REACHED|SEEN) *(.*?)(?= *&| *\\))(?: *& +(" +
                ".+))?\\)( +new)?( +automatic)?( +optional)?(?: *: *("+regexNomeDescr+"+))?(?:"+regexEndLine+"|$)",
                Pattern.CASE_INSENSITIVE).matcher(line);
        targetMatcher.find();
        String targName = targetMatcher.group(1);
        String targType = targetMatcher.group(2);
        String selectorType = targetMatcher.group(3);
        String selectorParam = targetMatcher.group(4);
        if(selectorParam==null) selectorParam = "";

        Matcher selectorParamMatcher;
        Tuple<String, Selector> selector = null; // This should always be updated, a
        // nullpointerexception is desired if it isn't

        switch (selectorType.toLowerCase()){
            case "in":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(1),new ContainedSelector());
                break;
            case "is":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(1),new IsSelector());
                break;
            case "has":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(1),new HasSelector());
                break;
            case "seen":
                selectorParamMatcher = Pattern.compile("(\\w+)").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(1),new VisibleSelector());
                break;

            case "distant":
                selectorParamMatcher = Pattern.compile(" *\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+) *").matcher(selectorParam);
                selectorParamMatcher.find();
                selector = new Tuple<>(selectorParamMatcher.group(3),
                        new DistanceSelector(Integer.parseInt(selectorParamMatcher.group(1)),
                                Integer.parseInt(selectorParamMatcher.group(2)), true));
                break;
            case "distantph":
                selectorParamMatcher = Pattern.compile(" *\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+) *").matcher(selectorParam);
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
            case "exists":
                selector = new Tuple<>("self", new ExistSelector());
                break;
            default:
                break;
        }

        String conditions = targetMatcher.group(5);
        List<Tuple<String, Condition>> conditionsList = new ArrayList<>();
        if(conditions==null) {
            conditions = "";
        } else {
            Matcher conditionsMatcher = Pattern.compile(" *(?:(NOT|not) +)?" +
                    "(DISTANT|DISTANTPH|HAS|IN" +
                    "|REACHES|SEEN) +(.+?) *(?:&|$)").matcher(conditions);


            conditionsList = conditionsMatcher.results().map(m -> {
                Matcher conditionParamMatcher;
                Condition condition;
                Tuple<String, Condition> toReturn = null;
                switch (m.group(2).toLowerCase()){
                    case "in":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new InCondition(m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "has":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new HasCondition(m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "seen":
                        conditionParamMatcher = Pattern.compile("(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition = new SeenCondition(m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(1),condition);
                        break;

                    case "distant":
                        conditionParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition =
                                new DistantCondition(Integer.parseInt(conditionParamMatcher.group(1)), Integer.parseInt(conditionParamMatcher.group(2)), true,m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(3),condition);
                        break;
                    case "distantph":
                        conditionParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition =
                                new DistantCondition(Integer.parseInt(conditionParamMatcher.group(1)), Integer.parseInt(conditionParamMatcher.group(2)), false,m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(3),condition);
                        break;
                    case "reaches":
                        conditionParamMatcher = Pattern.compile("\\( *([1-9]?\\d+) *, *([1-9]?\\d+) *\\) +(\\w+)").matcher(m.group(3));
                        conditionParamMatcher.find();
                        condition =
                                new ReachesCondition(Integer.parseInt(conditionParamMatcher.group(1)), Integer.parseInt(conditionParamMatcher.group(2)), m.group(1)!=null);
                        toReturn = new Tuple<>(conditionParamMatcher.group(3),condition);
                        break;

                    default:
                        try {
                            throw new Exception("No match");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }
                return toReturn;
            }).collect(Collectors.toList());




        }

        String isNew = targetMatcher.group(6);
        String isAuto = targetMatcher.group(7);
        String isOpt = targetMatcher.group(8);
        String descr = targetMatcher.group(9);
        if (descr==null)
            descr="";

        return new Tuple<>(targName, new TargeterTemplate(selector,conditionsList,targType,
                isOpt!=null,isNew!=null, isAuto!=null, descr));
    }

    private static EffectTemplate parseEffect(String body){

        EffectTemplate effect = null;
        Matcher effectMatcher = Pattern.compile("effect +(FIRE|DAMAGE|RELOAD|GRAB|MARK|MOVE|PAY) " +
                "*(.*)(?:$|"+regexEndLine+")", Pattern.CASE_INSENSITIVE).matcher(body);
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
                effectParamMatcher =
                        Pattern.compile(" *(\\w+) to +(\\w+)").matcher(effectMatcher.group(2));
                effectParamMatcher.find();
                effect = new MoveTemplate(effectParamMatcher.group(1), effectParamMatcher.group(2));
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
