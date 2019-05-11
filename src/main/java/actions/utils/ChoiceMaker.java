package actions.utils;

import controllerresults.ControllerActionResult;
import actions.targeters.targets.Targetable;
import genericitems.Tuple;

import java.util.List;
import java.util.function.Function;

/**
 * TODO: gestire il caso in cui il target sia opzionale o unico
 * TODO: generare la nuova azione e restituire la risposta per il controller
 */
public interface ChoiceMaker {
    /**
     * This function will be called by the Targeter
     */
    void giveTargets(String targetId, List<Targetable> possibilities,
                     Function<Integer, Targetable> action);

    /**
     * This function will be called by the controller
     */
    Tuple<Boolean, List<Targetable>> showOptions();

    /**
     *
     */
    ControllerActionResult pick(int choice);
}
