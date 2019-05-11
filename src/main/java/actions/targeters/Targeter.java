package actions.targeters;

import actions.conditions.Condition;
import actions.selectors.Selector;
import actions.targeters.targets.DirectionTarget;
import actions.targeters.targets.GroupTarget;
import actions.targeters.targets.Targetable;
import actions.utils.ChoiceMaker;
import actions.utils.NotEnoughTargetsException;
import board.Sandbox;
import genericitems.Tuple;
import uid.TileUID;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class, one per action, generates the mapping from string to Target used by the effects
 * If interactivity is needed it will call on a class given during instantiation
 */
public class Targeter {


    /**
     * The Map containing the functions that will be used by the selector
     */
    private static  Map<String, Function<Sandbox, Function<TileUID, Stream<Targetable>>>> targetBuilders = new HashMap<>();
    static {
        targetBuilders.put("pawn",sandbox-> tileUID -> sandbox.containedPawns(tileUID).stream().map(sandbox::getBasic));
        targetBuilders.put("tile",sandbox-> tileUID -> Stream.of(sandbox.getTile(tileUID)));
        targetBuilders.put("direction_l",sandbox-> tileUID -> sandbox.neighbors(tileUID, true).entrySet().stream().map(e -> new DirectionTarget(sandbox, e.getValue(),e.getKey(),true)));
        targetBuilders.put("direction_p",sandbox-> tileUID -> sandbox.neighbors(tileUID, false).entrySet().stream().map(e -> new DirectionTarget(sandbox, e.getValue(),e.getKey(),false)));
        targetBuilders.put("room",sandbox-> tileUID -> Stream.of(sandbox.getRoom(sandbox.room(tileUID))));
    }

    private static String groupString = "group";

    /**
     * The sandbox of the weapon use currently being explored
     */
    private final Sandbox sandbox;

    /**
     * {@link TargeterTemplate#selector}
     */
    private final Tuple<String, Selector> selector;

    /**
     * {@link TargeterTemplate#filters}
     */
    private final List<Tuple<String, Condition>> filters;

    /**
     * The {@link ChoiceMaker} interface represents an entity (player or computer) making a choice
     * between multiple targets when needed
     * @see ChoiceMaker
     */
    private final ChoiceMaker master;

    /**
     * The targets acquired prior to this Targeter being run
     */
    private final Map<String, Targetable> previousTargets;


    /**
     * {@link TargeterTemplate#newTarg}
     */
    private final boolean newTarg;

    /**
     * The ID of the target
     */
    private final String targetID;

    /**
     * @see Targeter#targetBuilders
     */
    private final String type;

    /**
     * {@link TargeterTemplate#optional}
     */
    private final boolean optional;

    private final Predicate<Targetable> isNew;

    /**
     * This creates the targeter for an instance of the weapon being used
     * @param sandbox the sandbox linked with the weapon use
     * @param template the targeter template
     * @param master the ChoiceMaker to confirm targets
     * @param previousTargets a Map of previous targets
     */
    public Targeter(Sandbox sandbox,
                    ChoiceMaker master,
                    Map<String, Targetable> previousTargets,
                    TargeterTemplate template, String targetID){

        this.sandbox = sandbox;
        this.selector = template.selector;
        this.filters = template.filters;
        this.master = master;
        this.previousTargets = previousTargets;
        this.newTarg = template.newTarg;
        this.targetID = targetID;
        if (template.type.equals(groupString)){
            this.optional = false;
            this.type = "pawn";
        } else {
            this.type = template.type;
            this.optional = template.optional;
        }
        this.isNew = target -> previousTargets.values().contains(target);
    }

    /**
     * The main purpose of the Targeter, this class will:
     * 1. Generate, via {@link Selector#select(Sandbox, Targetable, Function)} a list of viable
     * targets
     * 2. Filter it using {@link Condition#checkTarget(Sandbox, Targetable, Targetable)}
     * 3. Check other conditions:
     *      a. If the target is new
     *      b. If it is optional
     * 4. If the selection is not automatic it then asks the {@link Targeter#master} to pick one
     * of many and returns it
     *
     * @throws NotEnoughTargetsException is thrown when the target is mandatory but no target
     * exists which satisfies the conditions
     */
    public void giveChoices() throws NotEnoughTargetsException {
        String funType = type;
        if (type.equals(groupString)) {
            funType = "pawn";
        }

        Function<TileUID, Stream<Targetable>> fun = targetBuilders.get(funType).apply(sandbox);

        Stream<Targetable> targets =
                selector.y.select(sandbox,previousTargets.get(selector.x), fun).stream();

        for (Tuple<String, Condition> i: filters){
            targets = targets.filter(t -> i.y.checkTarget(sandbox,t, previousTargets.get(i.x)));
        }

        List<Targetable> validTargets = new LinkedList<>(targets.collect(Collectors.toList()));

        if (type.equals(groupString) && !validTargets.isEmpty()) {
            GroupTarget t = new GroupTarget(validTargets.stream().flatMap(i -> i.getSelectedPawns(sandbox).stream()).collect(Collectors.toList()));
            validTargets = List.of(t);
        }

        if (newTarg)
            validTargets = validTargets.stream().filter(isNew).collect(Collectors.toList());

        if (!optional && validTargets.isEmpty()){
            throw new NotEnoughTargetsException("0 targets available in non optional targeter");
        }

        final List<Targetable> passedTargets = new ArrayList<>(validTargets);

        master.giveTargets(targetID, validTargets, integer -> {
            if (optional && integer<0)
                return null;
            else if (integer >= passedTargets.size()){
                return passedTargets.get(passedTargets.size()-1);
            } else return passedTargets.get(integer);
        });

    }

}
