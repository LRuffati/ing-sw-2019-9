package player;

import actions.ActionInfo;
import actions.ActionTemplate;
import actions.effects.*;
import actions.selectors.DistanceSelector;
import actions.targeters.TargeterTemplate;
import actions.utils.AmmoAmount;
import genericitems.Tuple;

import java.util.*;

class ActionTemplateOptions {

    private static final ActionTemplate muovi3 = new ActionTemplate(
            new ActionInfo(
                    "Muoviti di 3",
                    "muovi3",
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
                                            new DistanceSelector(1,3,true)
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
                                            new DistanceSelector(1,4,true)
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

    public static List<List<ActionTemplate>> getActionsStandard(int n){
        List<ActionTemplate> ret = new ArrayList<>(List.of(muovi3, grab, shoot));
        if (n>2)
            ret.add(muovi2grab);
        if (n>5)
            ret.add(muovi1spara);
        return List.of(ret, ret);
    }

    public static List<List<ActionTemplate>> getFrenzyActions(boolean beforeFirst,
                                                             boolean boardReset,
                                                        int n){
        List<ActionTemplate> ret = new ArrayList<>();
        if (!boardReset){
            if (n>2)
                ret.add(muovi2grab);
            if (n>5)
                ret.add(muovi1spara);
        }

        if (beforeFirst){
            ret.addAll(List.of(muoviricspara,muovi4,muovi2grab));
            return List.of(ret,ret);
        } else {
            ret.addAll(List.of(muovi2ricspara,muovi3grab));
            return List.of(ret);
        }
    }

}
