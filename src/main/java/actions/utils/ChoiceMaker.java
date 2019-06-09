package actions.utils;

import controllerresults.ControllerActionResultServer;
import actions.targeters.targets.Targetable;
import genericitems.Tuple;
import testcontroller.controllermessage.ControllerMessage;
import viewclasses.TargetView;

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
    void giveTargets(String targetId, List<TargetView> possibilities,
                     Function<Integer, Targetable> action);

    /**
     * This function will be called by the controller
     */
    Tuple<Boolean, List<TargetView>> showOptions();

    /**
     *
     */
    ControllerMessage pick(int choice);
}
