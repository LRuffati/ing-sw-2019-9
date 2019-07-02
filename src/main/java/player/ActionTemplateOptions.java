package player;

import actions.ActionInfo;
import actions.ActionTemplate;
import actions.effects.*;
import actions.selectors.DistanceSelector;
import actions.targeters.TargeterTemplate;
import actions.utils.AmmoAmount;
import genericitems.Tuple;

import java.util.*;

/**
 * This class contains all the actions that can be performed by an actor during his turn.
 * It covers all the possible cases, Normal game, FF and intermediate damage phases
 */
class ActionTemplateOptions {
    ActionTemplateOptions(){}

    private static final ActionTemplate muovi3 = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 3",
                    "muovi3",
                    "Muovi di 3",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,3,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli in quale cella spostarti"
                            )
                    )
            ),
            List.of(
                    new MoveTemplate("self", "destination")
            )
    );

    private static final ActionTemplate muovi4 = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 4",
                    "muovi4",
                    "muovi di 4",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,4,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli in quale cella spostarti"
                            )
                    )
            ),
            List.of(
                    new MoveTemplate("self", "destination")
            )
    );

    private static final ActionTemplate grab = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 1 e raccogli",
                    "muovigrab",
                    "muovi di uno e raccogli",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,1,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli in quale cella raccogliere"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new GrabTemplate()
            )
    );

    private static final ActionTemplate shoot = new ActionTemplate(
            new ActionInfo(
                    "Spara",
                    "spara",
                    "spara",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(),
            List.of(new Fire())
    );

    private static final ActionTemplate muovi2grab = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 2 e raccogli",
                    "muovi2grab",
                    "muovi di due e raccogli",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,2,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli in quale cella raccogliere"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new GrabTemplate()
            )
    );

    private static final ActionTemplate muovi1spara = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 1 e spara",
                    "muovishoot",
                    "muovi di uno e spara",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,1,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli da quale cella sparare"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new Fire()
            )
    );

    private static final ActionTemplate muoviricspara = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 1, ricarica e spara",
                    "muovirelshoot",
                    "Muoviti di 1, ricarica e spara",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,1,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli da quale cella sparare"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new ReloadTemplate(),
                    new Fire()
            )
    );

    private static final ActionTemplate muovi2ricspara = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 2, ricarica e spara",
                    "muovi2ricshoot",
                    "Muoviti di 2, ricarica e spara",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,2,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli da quale cella sparare"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new ReloadTemplate(),
                    new Fire()
            )
    );

    private static final ActionTemplate muovi3grab = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 3 e raccogli",
                    "muovi3grab",
                    "muovi di tre e raccogli",
                    new AmmoAmount(),
                    List.of(),
                    List.of(),
                    Optional.empty(),
                    true
            ),
            List.of(
                    new Tuple<>(
                            "destination",
                            new TargeterTemplate(
                                    new Tuple<>(
                                            "self",
                                            new DistanceSelector(0,3,true)
                                    ),
                                    List.of(),
                                    "tile",
                                    false,
                                    false,
                                    false,
                                    "Scegli in quale cella raccogliere"
                                )
                    )
                    ),
            List.of(
                    new MoveTemplate("self", "destination"),
                    new GrabTemplate()
            )
    );

    /**
     * It builds the list of action available during the first phase of the game (non-frenzy)
     * @param n damage received by the player
     * @return A List containing a List of all the available {@link actions.ActionTemplate actions}
     */
    static List<List<ActionTemplate>> getActionsStandard(int n){
        List<ActionTemplate> ret = new ArrayList<>(List.of(muovi3, grab, shoot));
        if (n>2) {
            ret.add(ret.indexOf(grab), muovi2grab);
            ret.remove(grab);
        }
        if (n>5) {
            ret.add(ret.indexOf(shoot), muovi1spara);
            ret.remove(shoot);
        }
        return List.of(ret, ret);
    }

    /**
     * It builds the list of action available during the second phase of the game (FinalFrenzy)
     * @param beforeFirst true if the actor will play before the player that started FF
     * @return A List containing a List of all the available {@link actions.ActionTemplate actions}
     */
    static List<List<ActionTemplate>> getFrenzyActions(boolean beforeFirst) {
        List<ActionTemplate> ret = new ArrayList<>();

        if(beforeFirst) {
            ret.addAll(List.of(muovi4, muovi2grab, muoviricspara));
            return List.of(ret,ret);
        }
        else {
            ret.addAll(List.of(muovi2ricspara,muovi3grab));
            return List.of(ret);
        }
    }

}
